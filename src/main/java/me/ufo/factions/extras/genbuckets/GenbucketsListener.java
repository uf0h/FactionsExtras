package me.ufo.factions.extras.genbuckets;

import me.ufo.architect.util.NBTItem;
import me.ufo.architect.util.Style;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.buckets.Bucket;
import me.ufo.factions.extras.genbuckets.buckets.Genbucket;
import me.ufo.factions.extras.genbuckets.buckets.impl.Buckets;
import me.ufo.factions.extras.genbuckets.generation.GenerationType;
import me.ufo.factions.extras.genbuckets.generation.types.Horizontal;
import me.ufo.factions.extras.genbuckets.generation.types.Vertical;
import me.ufo.factions.extras.genbuckets.gui.impl.BucketsGUI;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

public class GenbucketsListener implements Listener {

  private final Extras plugin;
  private final Genbuckets genbuckets;

  public GenbucketsListener(final Extras plugin, final Genbuckets genbuckets) {
    this.plugin = plugin;
    this.genbuckets = genbuckets;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerBucketEmptyEvent(final PlayerBucketEmptyEvent event) {
    final ItemStack item = event.getPlayer().getItemInHand();
    if (item != null && item.hasItemMeta()) {
      event.setCancelled(true);
      final NBTItem nbtItem = new NBTItem(item);

      if (nbtItem.hasKey("GENBUCKET")) {
        final Player player = event.getPlayer();
        final String value = nbtItem.getString("GENBUCKET");
        final Material material = Material.valueOf(value.split("_")[1]);

        Genbucket bucket = null;
        // vertical
        if ('V' == value.charAt(0)) {
          final Block block = event.getBlockClicked().getRelative(event.getBlockFace());

          if (block.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
            return;
          }

          for (final Bucket b : this.genbuckets.getBuckets().buckets()) {
            if (b instanceof Genbucket) {
              final Genbucket gb = (Genbucket) b;
              if (gb.getGenerationType() == GenerationType.VERTICAL && gb.getMaterial() == material) {
                bucket = gb;
                break;
              }
            }
          }

          if (bucket == null) {
            return;
          }

          if (bucket.getMaterial().hasGravity() && block.getLocation().getY() <= 4) {
            this.plugin.getDepends().deposit(player, bucket.getCostOfPlacement());
            return;
          }

          if (!this.plugin.getDepends().withdraw(player, bucket.getCostOfPlacement())) {
            final double difference =
              bucket.getCostOfPlacement() - this.plugin.getDepends().getBalance(player);

            player
              .sendMessage(this.genbuckets.getMessages().notEnoughForPlacement(difference, bucket.getName()));
            return;
          }

          final Genbucket copy = bucket;
          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final PacketPlayOutChat packet = new PacketPlayOutChat(
              new ChatComponentText(Style.translate("&c-$" + copy.getCostOfPlacement())), (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
          });

          this.genbuckets.getGenerationTask().addGeneration(new Vertical(bucket.getMaterial(), block));
        }
        // horizontal
        else {
          for (final Bucket b : this.genbuckets.getBuckets().buckets()) {
            if (b instanceof Genbucket) {
              final Genbucket gb = (Genbucket) b;
              if (gb.getGenerationType() == GenerationType.HORIZONTAL && gb.getMaterial() == material) {
                bucket = gb;
                break;
              }
            }
          }

          if (bucket == null) {
            return;
          }

          if (event.getBlockFace() == BlockFace.UP || event.getBlockFace() == BlockFace.DOWN) {
            return;
          }

          if (!this.plugin.getDepends().withdraw(player, bucket.getCostOfPlacement())) {
            final double difference =
              bucket.getCostOfPlacement() - this.plugin.getDepends().getBalance(player);

            player
              .sendMessage(this.genbuckets.getMessages().notEnoughForPlacement(difference, bucket.getName()));
            return;
          }

          final Genbucket copy = bucket;
          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final PacketPlayOutChat packet = new PacketPlayOutChat(
              new ChatComponentText(Style.translate("&c-$" + copy.getCostOfPlacement())), (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
          });

          this.genbuckets.getGenerationTask().addGeneration(
            new Horizontal(player, bucket.getMaterial(),
                           event.getBlockClicked().getRelative(event.getBlockFace()),
                           event.getBlockFace()));
        }
      } else if (nbtItem.hasKey("WATERBUCKET")) {
        final Player player = event.getPlayer();
        final Bucket bucket = this.genbuckets.getBuckets().getUnlimitedWaterBucket();
        event.setCancelled(true);

        if (!this.plugin.getDepends().withdraw(player, bucket.getCostOfPlacement())) {
          final double difference = bucket.getCostOfPlacement() - this.plugin.getDepends().getBalance(player);

          player
            .sendMessage(this.genbuckets.getMessages().notEnoughForPlacement(difference, bucket.getName()));
          return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
          final PacketPlayOutChat packet = new PacketPlayOutChat(
            new ChatComponentText(Style.translate("&c-$" + bucket.getCostOfPlacement())), (byte) 2);
          ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });

        event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.WATER, false);
      }
    }
  }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onInventoryClickEvent(final InventoryClickEvent event) {
    if (event.getClickedInventory() == null) {
      return;
    }

    if (event.getClickedInventory().getHolder() instanceof BucketsGUI) {
      event.setCancelled(true);

      this.genbuckets.getBucketsGUI().onClick((Player) event.getWhoClicked(), event.getRawSlot());
    }
  }

}

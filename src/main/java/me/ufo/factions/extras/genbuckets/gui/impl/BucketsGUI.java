package me.ufo.factions.extras.genbuckets.gui.impl;

import java.util.HashMap;
import me.ufo.architect.util.Style;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.Genbuckets;
import me.ufo.factions.extras.genbuckets.buckets.Bucket;
import me.ufo.factions.extras.genbuckets.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BucketsGUI implements GUI, InventoryHolder {

  private final Extras plugin = Extras.get();
  private final Genbuckets genbuckets = Genbuckets.get();
  private final HashMap<Integer, ItemStack> bucketSlots = new HashMap<>();
  private Inventory bucketsGUI;

  @Override
  public void build() {
    final ConfigurationSection config = this.plugin.getConfig().getConfigurationSection("genbuckets.gui");
    final String title = Style.translate(config.getString("title"));
    final int size = config.getInt("size");
    final int glassColor = config.getInt("stained-glass");
    final ItemStack glassFiller = getGlassItem(glassColor);

    bucketsGUI = Bukkit.createInventory(this, size, title);

    for (int i = 0; i < size; i++) {
      bucketsGUI.setItem(i, glassFiller);
    }

    bucketSlots.forEach((slot, bucket) -> bucketsGUI.setItem(slot, bucket));
  }

  @Override
  public void onClick(final Player player, final int slot) {
    if (bucketSlots.containsKey(slot)) {

      for (final Bucket bucket : Genbuckets.get().getBuckets().buckets()) {
        if (bucketSlots.get(slot).equals(bucket.getItemStack())) {
          if (transact(player, bucket)) {
            player.getInventory().addItem(bucket.getItemStack().clone());
            player.sendMessage(
              Style.replace(genbuckets.getMessages().GIVEN_BUCKET, "%bucket%", bucket.getName()));
            break;
          }
        }
      }

    }
  }

  @Override
  public boolean transact(final Player player, final Bucket bucket) {
    final double cost = bucket.getCostOfPurchase();
    if (this.plugin.getDepends().withdraw(player, cost)) {
      return true;
    } else {
      final double difference = cost - this.plugin.getDepends().getBalance(player);

      player.sendMessage(this.genbuckets.getMessages().notEnoughForPlacement(difference, bucket.getName()));
      return false;
    }
  }

  @Override
  public void openGUI(final Player player) {
    player.openInventory(bucketsGUI);
  }

  @Override
  public Inventory getInventory() {
    return bucketsGUI;
  }

  public HashMap<Integer, ItemStack> getBucketSlots() {
    return bucketSlots;
  }

  private ItemStack getGlassItem(final int color) {
    final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) color);
    final ItemMeta glassMeta = glass.getItemMeta();
    glassMeta.setDisplayName(" ");
    glass.setItemMeta(glassMeta);
    return glass;
  }

}

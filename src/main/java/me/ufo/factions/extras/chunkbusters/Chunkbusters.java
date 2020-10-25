package me.ufo.factions.extras.chunkbusters;

import me.ufo.architect.util.Item;
import me.ufo.architect.util.NBTItem;
import me.ufo.architect.util.Style;
import me.ufo.factions.extras.Extras;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class Chunkbusters implements Listener {

  private final Extras plugin;

  private final String CHUNKBUSTER_PLACED;
  private final ItemStack CHUNKBUSTER;

  public Chunkbusters(final Extras plugin) {
    this.plugin = plugin;

    final ConfigurationSection config = plugin.getConfig().getConfigurationSection("chunkbusters");
    this.CHUNKBUSTER = new NBTItem(
      Item.fromConfig(config.getConfigurationSection("item"))
    ).set("CHUNKBUSTER", true).asItemStack();

    final ConfigurationSection lang = config.getConfigurationSection("lang");
    this.CHUNKBUSTER_PLACED = Style.translate(lang.getString("placed"));

    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    plugin.getCommand("chunkbusters").setExecutor(new ChunkbustersCommand(this));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockPlaceEvent(final BlockPlaceEvent event) {
    final Player player = event.getPlayer();
    final ItemStack item = player.getItemInHand();

    if (item == null) {
      return;
    }

    if (item.getType() != CHUNKBUSTER.getType()) {
      return;
    }

    if (!Item.hasKey(item, "CHUNKBUSTER")) {
      return;
    }

    player.sendMessage(CHUNKBUSTER_PLACED);

    Bukkit.getScheduler().runTaskLater(
      plugin,
      () -> {
        plugin.getDepends().clearChunk(1, event.getBlockPlaced().getLocation());
      }, 100L
    );
  }

  public ItemStack getItem() {
    return CHUNKBUSTER;
  }

}

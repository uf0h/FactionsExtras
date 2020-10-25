package me.ufo.factions.extras.tools;

import me.ufo.architect.util.Item;
import me.ufo.architect.util.NBTItem;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.tools.sell.SellWand;
import me.ufo.factions.extras.tools.tray.TrayPickaxe;
import me.ufo.factions.extras.tools.trench.TrenchPickaxe;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class Tools implements Listener {

  private final Extras plugin;

  private TrayPickaxe trayPickaxe;
  private TrenchPickaxe trenchPickaxe;
  private SellWand sellWand;

  public Tools(final Extras plugin) {
    this.plugin = plugin;

    final ConfigurationSection config = plugin.getConfig().getConfigurationSection("tools");

    for (final Tool tool : Tool.values) {
      final ConfigurationSection toolConfig = config.getConfigurationSection(tool.name());

      if (toolConfig == null) {
        continue;
      }

      if (!toolConfig.getBoolean("enabled", false)) {
        continue;
      }

      final NBTItem item = new NBTItem(Item.fromConfig(toolConfig)).set(tool.name(), true);

      tool.setItem(item.asItemStack());

      switch (tool) {
        default:
          continue;

        case TRAY_PICKAXE:
          this.trayPickaxe = new TrayPickaxe();

        case TRENCH_PICKAXE:
          this.trenchPickaxe = new TrenchPickaxe();

        case SELL_WAND:
          this.sellWand = new SellWand(plugin);
      }
    }

    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    plugin.getCommand("tools").setExecutor(new ToolsCommand());
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(final BlockBreakEvent event) {
    final Player player = event.getPlayer();
    final ItemStack item = player.getItemInHand();

    if (item == null || !item.hasItemMeta()) {
      return;
    }

    boolean foundType = false;
    for (final Tool tool : Tool.values) {
      if (tool.getItem() == null) {
        continue;
      }

      if (item.getType() == tool.getItem().getType()) {
        foundType = true;
      }
    }

    if (!foundType) {
      return;
    }

    final NBTItem nbtItem = new NBTItem(item);

    if (this.trayPickaxe != null && nbtItem.hasKey(Tool.TRAY_PICKAXE.name())) {
      event.setCancelled(true);

      this.trayPickaxe.onBlockBreak(
        player,
        event.getBlock().getLocation()
      );
    } else if (this.trenchPickaxe != null && nbtItem.hasKey(Tool.TRENCH_PICKAXE.name())) {
      event.setCancelled(true);

      this.trenchPickaxe.onBlockBreak(
        player,
        event.getBlock().getLocation()
      );
    }
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
      return;
    }

    final Player player = event.getPlayer();
    final ItemStack item = player.getItemInHand();

    if (item == null || !item.hasItemMeta()) {
      return;
    }

    if (item.getType() != Tool.SELL_WAND.getItem().getType()) {
      return;
    }

    if (!Item.hasKey(item, "SELL_WAND")) {
      return;
    }

    event.setCancelled(true);

    this.sellWand.onPlayerInteract(player, event.getClickedBlock().getLocation());
  }

}

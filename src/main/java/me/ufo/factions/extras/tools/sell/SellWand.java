package me.ufo.factions.extras.tools.sell;

import me.ufo.factions.extras.Extras;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class SellWand {

  private final Extras plugin;

  public SellWand(final Extras plugin) {
    this.plugin = plugin;
  }

  public void onPlayerInteract(final Player player, final Location location) {
    final Block block = location.getBlock();

    if (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) {
      return;
    }

    final Chest chest = (Chest) block.getState();
    final Inventory inventory = chest.getInventory();

    double totalCost = 0.0;

    final int size = inventory.getSize();
    for (int i = 0; i < size; i++) {
      if (inventory.getItem(i) == null) {
        continue;
      }

      final ItemStack item = inventory.getItem(i);

      final double cost = this.plugin.getDepends().getSellPrice(item);

      if (cost != -1.0) {
        totalCost += cost;
        inventory.setItem(i, new ItemStack(Material.AIR));
      }
    }

    if (totalCost > 0.0) {
      this.plugin.getDepends().deposit(player, totalCost);
      player.sendMessage(
        ChatColor.GREEN.toString() + "You sold the contents of this chest for $" + Extras.getDecimalFormat()
          .format(totalCost) + "."
      );
    } else {
      player.sendMessage(
        ChatColor.RED.toString() + "There are no sellable items in this chest."
      );
    }
  }

}

package me.ufo.factions.extras.chunkbusters;

import me.ufo.architect.util.Style;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ChunkbustersCommand implements CommandExecutor {

  private final Chunkbusters chunkbusters;
  private final String[] usage;

  public ChunkbustersCommand(final Chunkbusters chunkbusters) {
    this.chunkbusters = chunkbusters;
    this.usage = Style.translate(
      "&eGive chunkbuster to a player.",
      "&b/chunkbuster give &3<player> <amount>"
    );
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label,
                           final String[] args) {

    if (!sender.hasPermission("factions.chunkbusters.give")) {
      return false;
    }

    // chunkbusters give <player> <amount>
    if (args.length != 3) {
      sender.sendMessage(usage);
      return false;
    }

    if ("give".equalsIgnoreCase(args[0])) {
      final Player target = Bukkit.getPlayer(args[1]);
      if (target == null) {
        sender.sendMessage(ChatColor.RED.toString() + "Player not found.");
        return false;
      }

      final int amount;
      try {
        amount = Integer.parseInt(args[2]);
      } catch (final NumberFormatException ignored) {
        sender.sendMessage(ChatColor.RED.toString() + "Invalid number.");
        return false;
      }

      final ItemStack item = chunkbusters.getItem();
      item.setAmount(amount);

      target.getInventory().addItem(item);
    }

    return true;
  }

}

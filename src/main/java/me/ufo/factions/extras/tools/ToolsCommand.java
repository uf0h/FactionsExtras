package me.ufo.factions.extras.tools;

import me.ufo.architect.util.Style;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ToolsCommand implements CommandExecutor {

  private final String[] usage;

  public ToolsCommand() {
    this.usage = Style.translate(
      "&eGive tool to a player.",
      "&b/tools give &3<player> <tool> <amount>"
    );
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label,
                           final String[] args) {

    if (!sender.hasPermission("factions.tools.give")) {
      return false;
    }

    // tools give <player> <tool> <amount>
    if (args.length != 4) {
      sender.sendMessage(usage);
      return false;
    }

    if (!"give".equalsIgnoreCase(args[0])) {
      sender.sendMessage(usage);
      return false;
    }

    final Player target = Bukkit.getPlayer(args[1]);
    if (target == null) {
      sender.sendMessage(ChatColor.RED.toString() + "Player not found.");
      return false;
    }

    final Tool tool = Tool.get(args[2]);
    if (tool == null) {
      sender.sendMessage(ChatColor.RED.toString() + "That is not a valid tool.");
      return false;
    }

    final int amount;
    try {
      amount = Integer.parseInt(args[3]);
    } catch (final NumberFormatException ignored) {
      sender.sendMessage(ChatColor.RED.toString() + "Invalid number.");
      return false;
    }

    final ItemStack item = tool.getItem();
    item.setAmount(amount);

    target.getInventory().addItem(item);
    return true;
  }

}

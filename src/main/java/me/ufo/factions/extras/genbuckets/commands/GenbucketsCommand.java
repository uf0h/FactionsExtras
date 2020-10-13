package me.ufo.factions.extras.genbuckets.commands;

import me.ufo.factions.extras.genbuckets.Genbuckets;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenbucketsCommand implements CommandExecutor {

  private final Genbuckets genbuckets = Genbuckets.get();

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label,
                           final String[] args) {

    if (sender instanceof Player) {
      final Player player = (Player) sender;

      if (args.length == 0) {
        this.genbuckets.getBucketsGUI().openGUI(player);
      } else if (args.length == 3) {
        if (sender.isOp()) {
          // ... test command
          if ("size".equalsIgnoreCase(args[0])) {
            player.sendMessage(new String[] {
              "No. of generations: " + genbuckets.getGenerationTask().getSize(),
              "Current highest no. of generations p/t: " + genbuckets.getGenerationTask()
                .getHighestGenerations()
            });
          } else if ("debug".equalsIgnoreCase(args[0])) {
            genbuckets.setDebug(!genbuckets.isDebug());
            player.sendMessage("Toggled genbuckets debug mode.");
          }
        } else {
          Genbuckets.get().getBucketsGUI().openGUI(player);
        }
      } else {
        Genbuckets.get().getBucketsGUI().openGUI(player);
      }

      return true;
    }

    return false;
  }

}

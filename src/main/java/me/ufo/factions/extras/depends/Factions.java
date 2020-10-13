package me.ufo.factions.extras.depends;

import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.perms.PermissibleAction;
import me.ufo.factions.extras.Extras;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Factions {

  private boolean enabled;

  public Factions(final Extras plugin) {
    if (plugin.getServer().getPluginManager().getPlugin("Factions") != null){
      this.enabled = true;
    }
  }

  public boolean playerCanBuildHere(final Player player, final Location location) {
    if (!this.enabled) {
      return true;
    }

    if (player == null) {
      return true;
    }

    return FactionsBlockListener.playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD, true);
  }

}

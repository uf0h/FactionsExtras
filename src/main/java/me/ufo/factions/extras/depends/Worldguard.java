package me.ufo.factions.extras.depends;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.ufo.factions.extras.Extras;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Worldguard {

  private WorldGuardPlugin worldguard;

  private boolean enabled;

  public Worldguard(final Extras plugin) {
    if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
      this.enabled = true;
    }
  }

  public boolean playerCanPlaceHere(final Player player, final Location location) {
    if (!this.enabled) {
      return true;
    }

    if (player == null) {
      return true;
    }

    return this.worldguard.canBuild(player, location.getBlock());
  }

}

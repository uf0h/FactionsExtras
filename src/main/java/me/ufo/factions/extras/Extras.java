package me.ufo.factions.extras;

import me.ufo.factions.extras.chunkbusters.Chunkbusters;
import me.ufo.factions.extras.depends.Depends;
import me.ufo.factions.extras.genbuckets.Genbuckets;
import me.ufo.factions.extras.tools.Tools;
import org.bukkit.plugin.java.JavaPlugin;

public final class Extras extends JavaPlugin {

  private static Extras plugin;

  private boolean debug;
  private Depends depends;

  private Genbuckets genbuckets;

  @Override
  public void onEnable() {
    plugin = this;

    this.saveDefaultConfig();

    this.depends = new Depends(this);

    if (this.getConfig().getBoolean("tools.enabled")) {
      new Tools(this);
    }

    if (this.getConfig().getBoolean("genbuckets.enabled")) {
      this.genbuckets = new Genbuckets(this);
    }

    if (this.getConfig().getBoolean("chunkbusters.enabled")) {
      new Chunkbusters(this);
    }
  }

  @Override
  public void onDisable() {
    this.genbuckets.onDisable();
  }

  public static Extras get() {
    return plugin;
  }

  public boolean isDebugModeEnabled() {
    return debug;
  }

  public void setDebugMode(final boolean debug) {
    this.debug = debug;
  }

  public Depends getDepends() {
    return depends;
  }

}

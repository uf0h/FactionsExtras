package me.ufo.factions.extras.depends;

import me.ufo.factions.extras.Extras;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class Depends {

  private final Econ econ;
  private final ShopGuiPlus shopGuiPlus;
  private final Factions factions;
  private final WorldGuard worldguard;
  private final WorldEdit worldedit;

  public Depends(final Extras plugin) {
    this.econ = new Econ(plugin);
    this.shopGuiPlus = new ShopGuiPlus(plugin);
    this.factions = new Factions(plugin);
    this.worldguard = new WorldGuard(plugin);
    this.worldedit = new WorldEdit(plugin);
  }

  public boolean deposit(final Player player, final double cost) {
    return this.econ.deposit(player, cost);
  }

  public boolean withdraw(final Player player, final double cost) {
    return this.econ.withdraw(player, cost);
  }

  public double getBalance(final Player player) {
    return this.econ.getBalance(player);
  }

  public double getSellPrice(final ItemStack item) {
    return this.shopGuiPlus.getSellPrice(item);
  }

  public boolean playerCanBuildHere(final Player player, final Location location) {
    return this.factions.playerCanBuildHere(player, location) &&
           this.worldguard.playerCanPlaceHere(player, location);
  }

  public boolean clearChunk(final int lowerBound, final Location location) {
    return this.worldedit.clearChunk(lowerBound, location);
  }

}

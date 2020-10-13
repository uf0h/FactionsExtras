package me.ufo.factions.extras.depends;

import me.ufo.factions.extras.Extras;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class Econ {

  private Economy econ;

  public Econ(final Extras plugin) {
    this.setupEconomy(plugin);
  }

  private boolean setupEconomy(final Extras plugin) {
    if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }

    final RegisteredServiceProvider<Economy> rsp =
      plugin.getServer().getServicesManager().getRegistration(Economy.class);

    if (rsp == null) {
      return false;
    }

    econ = rsp.getProvider();
    return econ != null;
  }

  public boolean withdraw(final Player player, final double cost) {
    if (econ == null) {
      return true;
    }

    return econ.withdrawPlayer(player, cost).transactionSuccess();
  }

  public boolean deposit(final Player player, final double amount) {
    if (econ == null) {
      return true;
    }

    return econ.depositPlayer(player, amount).transactionSuccess();
  }

  public double getBalance(final Player player) {
    if (econ == null) {
      return -1.0;
    }

    return econ.getBalance(player);
  }

}

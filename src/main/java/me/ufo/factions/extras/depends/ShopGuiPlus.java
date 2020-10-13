package me.ufo.factions.extras.depends;

import me.ufo.factions.extras.Extras;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.inventory.ItemStack;

public final class ShopGuiPlus {

  private boolean enabled;

  public ShopGuiPlus(final Extras plugin) {
    if (ShopGuiPlusApi.getPlugin() != null) {
      enabled = true;
    }
  }

  public double getSellPrice(final ItemStack item) {
    if (!enabled) {
      return -1.0;
    }

    return ShopGuiPlusApi.getItemStackPriceSell(item);
  }

  public boolean isEnabled() {
    return enabled;
  }

}

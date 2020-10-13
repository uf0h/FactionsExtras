package me.ufo.factions.extras.tools;

import org.bukkit.inventory.ItemStack;

public enum Tool {

  TRENCH_PICKAXE,
  TRAY_PICKAXE,
  SELL_WAND,
  /* TODO: HARVESTER_HOE */
  HARVESTER_HOE;

  public static final Tool[] values = Tool.values();

  private ItemStack item;

  public void setItem(final ItemStack item) {
    this.item = item;
  }

  public ItemStack getItem() {
    return this.item;
  }

  public static Tool get(final String in) {
    switch (in.toUpperCase()) {
      default:
        return null;

      case "TRENCH":
      case "TRENCHPICK":
      case "TRENCHPICKAXE":
      case "TRENCH_PICKAXE":
        return TRENCH_PICKAXE;

      case "TRAY":
      case "TRAYPICK":
      case "TRAYPICKAXE":
      case "TRAY_PICKAXE":
        return TRAY_PICKAXE;

      case "SELL":
      case "SELLWAND":
      case "SELL_WAND":
        return SELL_WAND;
    }
  }

}

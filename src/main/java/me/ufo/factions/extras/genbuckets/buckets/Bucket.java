package me.ufo.factions.extras.genbuckets.buckets;

import org.bukkit.inventory.ItemStack;

public abstract class Bucket {

  private final String name;
  private final ItemStack itemStack;
  private final double costOfPurchase;
  private final double costOfPlacement;

  public Bucket(final String name, final ItemStack itemStack, final double costOfPurchase,
                final double costOfPlacement) {
    this.name = name;
    this.itemStack = itemStack;
    this.costOfPurchase = costOfPurchase;
    this.costOfPlacement = costOfPlacement;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public String getName() {
    return name;
  }

  public double getCostOfPurchase() {
    return costOfPurchase;
  }

  public double getCostOfPlacement() {
    return costOfPlacement;
  }

}

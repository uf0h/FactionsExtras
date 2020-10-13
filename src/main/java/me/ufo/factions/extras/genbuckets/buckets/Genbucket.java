package me.ufo.factions.extras.genbuckets.buckets;

import me.ufo.factions.extras.genbuckets.generation.GenerationType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Genbucket extends Bucket {

  private final GenerationType generationType;
  private final Material material;

  public Genbucket(final String name, final ItemStack itemStack, final GenerationType generationType,
                   final Material material, final double costOfPurchase,
                   final double costOfPlacement) {

    super(name, itemStack, costOfPurchase, costOfPlacement);
    this.generationType = generationType;
    this.material = material;
  }

  public GenerationType getGenerationType() {
    return generationType;
  }

  public Material getMaterial() {
    return material;
  }

}

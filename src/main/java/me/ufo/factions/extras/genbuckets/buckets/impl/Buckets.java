package me.ufo.factions.extras.genbuckets.buckets.impl;

import java.util.HashSet;
import java.util.Set;
import me.ufo.architect.util.Item;
import me.ufo.architect.util.NBTItem;
import me.ufo.architect.util.Style;
import me.ufo.architect.util.UUID;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.Genbuckets;
import me.ufo.factions.extras.genbuckets.buckets.Bucket;
import me.ufo.factions.extras.genbuckets.buckets.Genbucket;
import me.ufo.factions.extras.genbuckets.generation.GenerationType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class Buckets {

  private final Extras plugin;
  private final Genbuckets genbuckets;
  private final Set<Bucket> buckets = new HashSet<>();

  private Bucket UNLIMITED_WATER_BUCKET;

  public Buckets(final Extras plugin, final Genbuckets genbuckets) {
    this.plugin = plugin;
    this.genbuckets = genbuckets;
  }

  public void build() {
    final ConfigurationSection water =
      this.plugin.getConfig().getConfigurationSection("genbuckets.types.UNLIMITED_WATER_BUCKET");

    final NBTItem waterBucket = new NBTItem(Item.fromConfig(water))
      .set("WATERBUCKET", true)
      .set("Random", UUID.toString(java.util.UUID.randomUUID()));

    final String name = Style.translate(water.getString("name"));
    final int slot = water.getInt("gui-slot");
    final double costOfPurchase = water.getDouble("costOfPurchase");
    final double costOfPlacement = water.getDouble("costOfPlacement");

    UNLIMITED_WATER_BUCKET = new Bucket(name, waterBucket.asItemStack(), costOfPurchase, costOfPlacement) {
      @Override
      public ItemStack getItemStack() {
        return super.getItemStack();
      }

      @Override
      public String getName() {
        return super.getName();
      }

      @Override
      public double getCostOfPurchase() {
        return super.getCostOfPurchase();
      }

      @Override
      public double getCostOfPlacement() {
        return super.getCostOfPlacement();
      }
    };

    this.buckets.add(UNLIMITED_WATER_BUCKET);
    this.genbuckets.getBucketsGUI().getBucketSlots().put(slot, UNLIMITED_WATER_BUCKET.getItemStack());

    this.setupGenbuckets();
  }

  private void setupGenbuckets() {
    final ConfigurationSection vertical =
      this.plugin.getConfig().getConfigurationSection("genbuckets.types.VERTICAL");
    final ConfigurationSection horizontal =
      this.plugin.getConfig().getConfigurationSection("genbuckets.types.HORIZONTAL");

    vertical.getKeys(false).forEach(key -> {
      final ConfigurationSection bucketConfig = vertical.getConfigurationSection(key);
      final Material material = Material.getMaterial(key);
      final NBTItem bucket =
        new NBTItem(Item.fromConfig(bucketConfig))
          .set("GENBUCKET", "V_" + material.name())
          .set("Random", UUID.toString(java.util.UUID.randomUUID()));

      final int slot = bucketConfig.getInt("gui-slot");
      final double costOfPurchase = bucketConfig.getDouble("costOfPurchase");
      final double costOfPlacement = bucketConfig.getDouble("costOfPlacement");

      final ItemStack bucketItem = bucket.asItemStack();

      this.buckets.add(
        new Genbucket(bucketItem.getItemMeta().getDisplayName(), bucketItem, GenerationType.VERTICAL,
                      material, costOfPurchase, costOfPlacement)
      );
      this.genbuckets.getBucketsGUI().getBucketSlots().put(slot, bucketItem);
    });

    horizontal.getKeys(false).forEach(key -> {
      final ConfigurationSection bucketConfig = horizontal.getConfigurationSection(key);
      final Material material = Material.getMaterial(key);
      final NBTItem bucket =
        new NBTItem(Item.fromConfig(bucketConfig))
          .set("GENBUCKET", "H_" + material.name())
          .set("Random", UUID.toString(java.util.UUID.randomUUID()));

      final int slot = bucketConfig.getInt("gui-slot");
      final double costOfPurchase = bucketConfig.getDouble("costOfPurchase");
      final double costOfPlacement = bucketConfig.getDouble("costOfPlacement");

      final ItemStack bucketItem = bucket.asItemStack();

      this.buckets.add(
        new Genbucket(bucketItem.getItemMeta().getDisplayName(), bucketItem, GenerationType.HORIZONTAL,
                      material, costOfPurchase, costOfPlacement)
      );
      this.genbuckets.getBucketsGUI().getBucketSlots().put(slot, bucketItem);
    });
  }

  public Bucket getUnlimitedWaterBucket() {
    return UNLIMITED_WATER_BUCKET;
  }

  public Set<Bucket> buckets() {
    return buckets;
  }

}

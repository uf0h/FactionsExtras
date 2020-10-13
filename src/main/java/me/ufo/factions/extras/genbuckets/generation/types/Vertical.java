package me.ufo.factions.extras.genbuckets.generation.types;

import me.ufo.factions.extras.genbuckets.generation.Generation;
import me.ufo.factions.extras.genbuckets.generation.GenerationType;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Vertical extends Generation {

  public Vertical(final Material material, final Block block) {
    super(GenerationType.VERTICAL, material, block);

    if (material.hasGravity()) {
      block.setType(Material.COBBLESTONE, false);
    } else {
      block.setType(material, false);
    }
  }

  @Override
  public void generate() {
    final Block toGenerate = this.getBlock().getLocation().add(0, -getIndex(), 0).getBlock();

    if (!this.getMaterial().hasGravity()) {
      final Block belowToGenerate = toGenerate.getLocation().add(0, -1, 0).getBlock();

      setIndex(getIndex() + 1);

      if (belowToGenerate.getY() > 0 && (belowToGenerate.getType() == Material.AIR ||
                                         belowToGenerate.getType() == Material.WATER ||
                                         belowToGenerate.getType() == Material.STATIONARY_WATER)) {

        toGenerate.setType(this.getMaterial(), false);
      } else {
        toGenerate.setType(this.getMaterial(), false);
        this.setCompleted(true);
      }
    } else {
      final Block cobble = toGenerate.getLocation().add(0, -1, 0).getBlock();
      final Block belowCobble = cobble.getLocation().add(0, -1, 0).getBlock();

      setIndex(getIndex() + 1);

      if (belowCobble.getY() > 0 && (belowCobble.getType() == Material.AIR ||
                                     belowCobble.getType() == Material.WATER ||
                                     belowCobble.getType() == Material.STATIONARY_WATER)) {

        this.getBlock().setType(this.getMaterial(), false);
        toGenerate.setType(Material.COBBLESTONE, false);
        cobble.setType(Material.COBBLESTONE, false);
        toGenerate.setType(this.getMaterial(), false);
      } else {
        toGenerate.setType(this.getMaterial(), false);
        cobble.setType(this.getMaterial(), false);
        this.setCompleted(true);
      }
    }
  }

}

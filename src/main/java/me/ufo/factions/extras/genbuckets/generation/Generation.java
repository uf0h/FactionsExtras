package me.ufo.factions.extras.genbuckets.generation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public abstract class Generation {

  private final GenerationType generationType;
  private final Material material;
  private final Block block;
  private Player player;
  private BlockFace blockFace;
  private int index = 0;
  private boolean completed;

  public Generation(final GenerationType generationType, final Material material, final Block block) {
    this.generationType = generationType;
    this.material = material;
    this.block = block;
  }

  public Generation(final Player player, final GenerationType generationType, final Material material,
                    final Block block,
                    final BlockFace blockFace) {
    this.player = player;
    this.generationType = generationType;
    this.material = material;
    this.block = block;
    this.blockFace = blockFace;
  }

  public abstract void generate();

  public Player getPlayer() {
    return player;
  }

  public GenerationType getGenerationType() {
    return generationType;
  }

  public Material getMaterial() {
    return material;
  }

  public Block getBlock() {
    return block;
  }

  public BlockFace getBlockFace() {
    return blockFace;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(final int index) {
    this.index = index;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(final boolean completed) {
    this.completed = completed;
  }

  @Override
  public String toString() {
    if (generationType == GenerationType.VERTICAL) {
      return String.join(",",
                         generationType.name(),
                         block.getWorld().getName(),
                         material.name(),
                         "" + block.getX(),
                         "" + block.getY(),
                         "" + block.getZ(),
                         "" + index
      );
    } else {
      return String.join(",",
                         generationType.name(),
                         block.getWorld().getName(),
                         material.name(),
                         "" + block.getX(),
                         "" + block.getY(),
                         "" + block.getZ(),
                         "" + index,
                         blockFace.name()
      );
    }
  }

}

package me.ufo.factions.extras.genbuckets.generation.types;

import java.util.HashSet;
import java.util.Set;
import me.ufo.architect.util.Border;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.generation.Generation;
import me.ufo.factions.extras.genbuckets.generation.GenerationType;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Horizontal extends Generation {

  private final Set<Chunk> allowedChunks = new HashSet<>(10);

  public Horizontal(final Player player, final Material material, final Block block,
                    final BlockFace blockFace) {
    super(player, GenerationType.HORIZONTAL, material, block, blockFace);

    block.setType(material);

    setIndex(1);
  }

  public Horizontal(final Material material, final Block block, final BlockFace blockFace) {
    super(GenerationType.HORIZONTAL, material, block);

  }

  @Override
  public void generate() {
    Block toGenerate = this.getBlock();
    Block belowToGenerate = null;
    final Location toGenerateLocation = toGenerate.getLocation();

    switch (this.getBlockFace()) {
      case NORTH:
        toGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.NORTH, getIndex());
        belowToGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.NORTH, 1);

        toGenerateLocation.add(0, 0, getIndex());
        break;
      case EAST:
        toGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.EAST, getIndex());
        belowToGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.EAST, 1);

        toGenerateLocation.add(getIndex(), 0, 0);
        break;
      case SOUTH:
        toGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.SOUTH, getIndex());
        belowToGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.SOUTH, 1);

        toGenerateLocation.add(0, 0, getIndex());
        break;
      case WEST:
        toGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.WEST, getIndex());
        belowToGenerate = addVectorByBlockFace(toGenerateLocation, BlockFace.WEST, 1);

        toGenerateLocation.add(getIndex(), 0, 0);
        break;
    }

    setIndex(getIndex() + 1);

    if (toGenerate.getType() != Material.AIR) {
      this.setCompleted(true);
      return;
    }

    if (this.getIndex() <= 160 && belowToGenerate.getType() == Material.AIR) {
      // ... player is valid
      if (this.getPlayer() != null) {
        if (!Border.isOutsideOfBorder(belowToGenerate)) {
          if (!allowedChunks.contains(toGenerate.getChunk())) {
            if (Extras.get().getDepends().playerCanBuildHere(this.getPlayer(), belowToGenerate.getLocation())) {
              toGenerate.setType(this.getMaterial(), false);
              allowedChunks.add(toGenerate.getChunk());
            } else {
              this.setCompleted(true);
            }
          } else {
            toGenerate.setType(this.getMaterial(), false);
          }
        }
      }
      /// ... player is not valid, i.e. on restart
      else {
        if (!Border.isOutsideOfBorder(belowToGenerate)) {
          toGenerate.setType(this.getMaterial(), false);
        }
      }
    } else {
      toGenerate.setType(this.getMaterial(), false);
      this.setCompleted(true);
    }
  }

  private Block addVectorByBlockFace(final Location location, final BlockFace blockFace, final int index) {
    switch (blockFace) {
      case NORTH:
        return location.add(0, 0, -index).getBlock();
      case EAST:
        return location.add(index, 0, 0).getBlock();
      case SOUTH:
        return location.add(0, 0, index).getBlock();
      case WEST:
        return location.add(-index, 0, 0).getBlock();
    }
    return null;
  }

}

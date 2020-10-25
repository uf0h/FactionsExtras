package me.ufo.factions.extras.depends;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.ufo.factions.extras.Extras;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class WorldEdit {

  private boolean enabled;

  public WorldEdit(final Extras plugin) {
    if (setupWorldedit() && setupFAWE()) {
      this.enabled = true;
    }
  }

  private boolean setupWorldedit() {
    return Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null;
  }

  private boolean setupFAWE() {
    return Bukkit.getServer().getPluginManager().getPlugin("FastAsyncWorldEdit") != null;
  }

  public boolean clearChunk(final int lowerBound, final Location location) {
    if (!enabled) {
      return false;
    }

    final Chunk chunk = location.getChunk();
    final Block lowerBoundBlock = chunk.getBlock(0, lowerBound, 0);
    final Block upperBoundBlock = chunk.getBlock(15, 255, 15);

    final Region chunkRegion =
      new CuboidRegion(new Vector(lowerBoundBlock.getX(), lowerBoundBlock.getY(), lowerBoundBlock.getZ()),
                       new Vector(upperBoundBlock.getX(), upperBoundBlock.getY(), upperBoundBlock.getZ()));

    final EditSession editSession =
      (new EditSessionBuilder(FaweAPI.getWorld(location.getWorld().getName()))).fastmode(true).build();

    editSession.setBlocks(chunkRegion, new BaseBlock(0, 0));
    Bukkit.getScheduler().runTaskAsynchronously(Extras.get(), editSession::flushQueue);

    return true;
  }

}

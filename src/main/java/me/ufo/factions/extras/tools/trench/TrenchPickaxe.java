package me.ufo.factions.extras.tools.trench;

import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.perms.PermissibleAction;
import me.ufo.architect.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class TrenchPickaxe {

  private final int radius;

  public TrenchPickaxe() {
    this.radius = 3;
  }

  public void onBlockBreak(final Player player, final Location location) {
    int px = 0;
    for (int i = 0; i < this.radius; i++) {
      location.add(1, 0, 0);

      if (!FactionsBlockListener
        .playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD, true)) {
        continue;
      }

      px += 1;
    }

    location.subtract(this.radius, 0, 0);

    int pz = 0;
    for (int i = 0; i < this.radius; i++) {
      location.add(0, 0, 1);

      if (!FactionsBlockListener
        .playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD, true)) {
        continue;
      }

      pz += 1;
    }

    location.subtract(0, 0, this.radius);

    int nx = 0;
    for (int i = 0; i < this.radius; i++) {
      location.subtract(1, 0, 0);

      if (!FactionsBlockListener
        .playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD, true)) {
        continue;
      }

      nx += 1;
    }

    location.add(this.radius, 0, 0);

    int nz = 0;
    for (int i = 0; i < this.radius; i++) {
      location.subtract(0, 0, 1);

      if (!FactionsBlockListener
        .playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD, true)) {
        continue;
      }

      nz += 1;
    }

    location.add(0, 0, this.radius);

    final Location neg = location.clone().subtract(nx, this.radius, nz);
    final Location pos = location.clone().add(px, this.radius, pz);
    final Cuboid cuboid = new Cuboid(neg, pos);

    for (final Block block : cuboid.getBreakableBlocks()) {
      block.setType(Material.AIR, false);
    }
  }

  /*@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBlockBreak(final BlockBreakEvent event) {
    final Player player = event.getPlayer();
    final ItemStack item = player.getItemInHand();

    if (item == null || !item.hasItemMeta()) {
      return;
    }

    if (item.getType() != tool.getItem().getType()) {
      return;
    }

    if (!Item.hasKey(item, "TRENCH_PICKAXE")) {
      return;
    }

    event.setCancelled(true);

    final Location location = event.getBlock().getLocation();

    int px = 0;
    for (int i = 0; i < this.radius; i++) {
      location.add(1, 0, 0);

      if (!FactionsBlockListener.playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD,
      true)) {
        continue;
      }

      px += 1;
    }

    location.subtract(this.radius, 0, 0);

    int pz = 0;
    for (int i = 0; i < this.radius; i++) {
      location.add(0, 0, 1);

      if (!FactionsBlockListener.playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD,
      true)) {
        continue;
      }

      pz += 1;
    }

    location.subtract(0, 0, this.radius);

    int nx = 0;
    for (int i = 0; i < this.radius; i++) {
      location.subtract(1, 0, 0);

      if (!FactionsBlockListener.playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD,
      true)) {
        continue;
      }

      nx += 1;
    }

    location.add(this.radius, 0, 0);

    int nz = 0;
    for (int i = 0; i < this.radius; i++) {
      location.subtract(0, 0, 1);

      if (!FactionsBlockListener.playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD,
      true)) {
        continue;
      }

      nz += 1;
    }

    location.add(0, 0, this.radius);

    final Location neg = location.clone().subtract(nx, this.radius, nz);
    final Location pos = location.clone().add(px, this.radius, pz);
    final Cuboid cuboid = new Cuboid(neg, pos);

    for (final Block block : cuboid.getBlocksByNotAir()) {
      block.setType(Material.AIR, false);
    }
  }*/

}

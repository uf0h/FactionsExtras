package me.ufo.factions.extras.genbuckets.gui;

import me.ufo.factions.extras.genbuckets.buckets.Bucket;
import org.bukkit.entity.Player;

public interface GUI {

  void build();

  void onClick(Player player, int slot);

  void openGUI(Player player);

  boolean transact(Player player, Bucket bucket);

}

package me.ufo.factions.extras.genbuckets;

import java.util.LinkedList;
import java.util.List;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.buckets.impl.Buckets;
import me.ufo.factions.extras.genbuckets.commands.GenbucketsCommand;
import me.ufo.factions.extras.genbuckets.generation.Generation;
import me.ufo.factions.extras.genbuckets.generation.GenerationType;
import me.ufo.factions.extras.genbuckets.generation.types.Horizontal;
import me.ufo.factions.extras.genbuckets.generation.types.Vertical;
import me.ufo.factions.extras.genbuckets.gui.impl.BucketsGUI;
import me.ufo.factions.extras.genbuckets.io.DataFile;
import me.ufo.factions.extras.genbuckets.lang.Messages;
import me.ufo.factions.extras.genbuckets.task.GenerationTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public final class Genbuckets {

  private static Genbuckets instance;
  private final Extras plugin;
  private final Messages messages;
  private final DataFile dataFile;
  private final GenerationTask generationTask;
  private final Buckets buckets;
  private final BucketsGUI bucketsGUI;
  private boolean debug;

  public Genbuckets(final Extras plugin) {
    instance = this;
    this.plugin = plugin;
    this.messages = new Messages();
    this.messages.build();

    this.dataFile = new DataFile("genbuckets.yml");
    this.dataFile.saveDefault();

    this.generationTask = new GenerationTask();
    this.buckets = new Buckets(plugin, this);
    this.bucketsGUI = new BucketsGUI();

    this.buckets.build();
    this.bucketsGUI.build();

    this.loadGenerationsFromFile();

    plugin.getCommand("genbuckets").setExecutor(new GenbucketsCommand());
    plugin.getServer().getPluginManager().registerEvents(new GenbucketsListener(plugin, this), plugin);
  }

  public void onDisable() {
    final List<String> out = new LinkedList<>();

    for (final Generation generation : GenerationTask.getGenerations()) {
      if (generation == null || generation.isCompleted()) {
        continue;
      }

      out.add(generation.toString());
    }

    this.dataFile.getFileConfiguration().set("genbuckets", out);
    this.dataFile.save();
  }

  private void loadGenerationsFromFile() {
    final List<String> serialized = this.dataFile.getFileConfiguration().getStringList("genbuckets");
    if (serialized != null) {
      serialized.forEach(string -> {
        final String[] array = string.split(",");
        final GenerationType type = GenerationType.valueOf(array[0]);
        final World world = Bukkit.getWorld(array[1]);
        final Material material = Material.valueOf(array[2]);
        final int x = Integer.parseInt(array[3]);
        final int y = Integer.parseInt(array[4]);
        final int z = Integer.parseInt(array[5]);

        final Location location = new Location(world, x, y, z);
        final int index = Integer.parseInt(array[6]);

        final Generation generation;
        if (type == GenerationType.HORIZONTAL) {
          final BlockFace blockFace = BlockFace.valueOf(array[7]);

          generation = new Horizontal(material, location.getBlock(), blockFace);
          this.generationTask.add(generation);
        } else {
          generation = new Vertical(material, location.getBlock());
        }

        generation.setIndex(index);

        this.generationTask.add(generation);
      });

      this.plugin.getLogger().info("Found " + serialized.size() + " generations to run...");
      this.generationTask.runTask();
    }
  }

  public Messages getMessages() {
    return messages;
  }

  public GenerationTask getGenerationTask() {
    return generationTask;
  }

  public Buckets getBuckets() {
    return buckets;
  }

  public BucketsGUI getBucketsGUI() {
    return bucketsGUI;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(final boolean debug) {
    this.debug = debug;
  }

  public static Genbuckets get() {
    return instance;
  }

}

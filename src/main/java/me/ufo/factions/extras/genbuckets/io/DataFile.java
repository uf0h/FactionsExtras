package me.ufo.factions.extras.genbuckets.io;

import java.io.File;
import java.io.IOException;
import me.ufo.factions.extras.Extras;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataFile {

  private final File file;
  private FileConfiguration fileConfiguration;

  public DataFile(final String fileName) {
    file = new File(Extras.get().getDataFolder().toString() + "/" + fileName);
  }

  public FileConfiguration getFileConfiguration() {
    if (fileConfiguration == null) {
      fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
    return fileConfiguration;
  }

  public void saveDefault() {
    if (!file.exists()) {
      Extras.get().saveResource(file.getName(), false);
    }
  }

  public void save() {
    if (fileConfiguration == null) {
      return;
    }

    try {
      fileConfiguration.save(file);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}

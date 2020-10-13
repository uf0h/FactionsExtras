package me.ufo.factions.extras.genbuckets.task;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.generation.Generation;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GenerationTask {

  private static final Set<Generation> generations = new LinkedHashSet<>();
  private static final AtomicInteger highestGenerations = new AtomicInteger(0);
  private static final long MAX_NS_PER_TICK = (long) (20 * 1E6);

  private final Extras INSTANCE = Extras.get();
  private BukkitTask task;

  public void runTask() {
    if (task == null) {
      task = this.generationTask().runTaskTimer(INSTANCE, 0L, 15L);
    }
  }

  public int getHighestGenerations() {
    return highestGenerations.get();
  }

  private BukkitRunnable generationTask() {
    return new BukkitRunnable() {
      @Override
      public void run() {
        final long stopTime = now() + MAX_NS_PER_TICK;

        if (!generations.isEmpty()) {
          final Iterator<Generation> iterator = generations.iterator();

          int generated = 0;
          while (iterator.hasNext() /*&& now() <= stopTime*/) {
            if (now() >= stopTime) {
              if (INSTANCE.isDebugModeEnabled()) {
                INSTANCE.getLogger().info("resting...");
              }
              break;
            }
            //INSTANCE.getLogger().info("running...");

            final Generation generation = iterator.next();

            if (generation.isCompleted()) {
              iterator.remove();
            } else {
              generation.generate();
              generated++;
            }

            if (highestGenerations.get() < generated) {
              highestGenerations.set(generated);
            }
          }
          if (INSTANCE.isDebugModeEnabled()) {
            INSTANCE.getLogger().info(
              "generated:(" + generated + "), skipped in this tick:" + "(" + (generations
                                                                                .size() - generated) + ")");
          }
        } else {
          GenerationTask.this.stop();
        }
      }
    };
  }

  public void stop() {
    if (INSTANCE.isDebugModeEnabled()) {
      INSTANCE.getLogger().info("STOPPING GENERATION TASK!");
    }
    task.cancel();
    task = null;
  }

  public void addGeneration(final Generation generation) {
    generations.add(generation);

    if (task == null) {
      this.runTask();
    }
  }

  public void add(final Generation generation) {
    generations.add(generation);
  }

  public int getSize() {
    return generations.size();
  }

  private static long now() {
    return System.nanoTime();
  }

  public static Set<Generation> getGenerations() {
    return generations;
  }

}

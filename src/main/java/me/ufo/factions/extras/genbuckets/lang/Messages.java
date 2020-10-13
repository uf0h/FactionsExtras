package me.ufo.factions.extras.genbuckets.lang;

import me.ufo.architect.util.Style;
import me.ufo.factions.extras.Extras;
import me.ufo.factions.extras.genbuckets.Genbuckets;

public class Messages {

  private final Extras plugin = Extras.get();
  private Genbuckets genbuckets = Genbuckets.get();
  public String GIVEN_BUCKET;
  private String NOT_ENOUGH_FOR_PURCHASE;
  private String NOT_ENOUGH_FOR_PLACEMENT;

  public void build() {
    GIVEN_BUCKET = Style.translate(plugin.getConfig().getString("genbuckets.lang.given-bucket"));
    NOT_ENOUGH_FOR_PURCHASE =
      Style.translate(plugin.getConfig().getString("genbuckets.lang.not-enough-for-purchase"));
    NOT_ENOUGH_FOR_PLACEMENT =
      Style.translate(plugin.getConfig().getString("genbuckets.lang.not-enough-for-placement"));
  }

  public String notEnoughForPlacement(final double difference, final String bucketName) {
    String out = Style.replace(
      this.genbuckets.getMessages().NOT_ENOUGH_FOR_PLACEMENT, "%difference%",
      Extras.getDecimalFormat().format(difference));

    out = Style.replace(out, "%bucket%", bucketName);

    return out;
  }

  public String notEnoughForPurchase(final double difference, final String bucketName) {
    String out = Style.replace(
      this.genbuckets.getMessages().NOT_ENOUGH_FOR_PURCHASE, "%difference%",
      Extras.getDecimalFormat().format(difference));

    out = Style.replace(out, "%bucket%", bucketName);

    return out;
  }

}

package util;

import java.time.ZoneId;
import javax.annotation.Nullable;
import net.iakovlev.timeshape.TimeZoneEngine;

/**
 * Map a location onto a timezone.
 */
public class LocationToTimeZoneConverter {

  private TimeZoneEngine engine;

  public LocationToTimeZoneConverter() {
    engine = TimeZoneEngine.initialize();
  }

  @Nullable
  public ZoneId findTimeZone(final double latitude, final double longitude) {
    return engine.query(latitude, longitude).orElse(null);
  }
}

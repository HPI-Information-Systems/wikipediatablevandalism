package features.context.impl;

import features.Feature;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import util.IpGeoLocator;
import util.LocationToTimeZoneConverter;

@Slf4j
@RequiredArgsConstructor
public class LocalizedTime implements Feature {

  private static final int UNKNOWN = -1;

  private final ChronoField unit;
  private final IpGeoLocator locator;
  private final LocationToTimeZoneConverter locationToTimeZoneConverter;

  @Override
  public double getValue(FeatureParameters parameters) {
    val contributor = parameters.getRevision().getContributor();

    if (!BasicUtils.isAnonymous(contributor)) {
      // No IP tracked for registered users
      return UNKNOWN;
    }

    val location = locator.findLocation(contributor.getIp());
    if (location == null) {
      log.debug("Cannot locate IP {}", contributor.getIp());
      return UNKNOWN;
    }

    val timeZone = locationToTimeZoneConverter
        .findTimeZone(location.getLatitude(), location.getLongitude());

    if (timeZone == null) {
      log.debug("Cannot determine timezone of {} (IP {})", location, contributor.getIp());
      return UNKNOWN;
    }

    final Date date = parameters.getRevision().getDate();
    final ZonedDateTime zoned = date.toInstant().atZone(timeZone);
    return zoned.get(unit);
  }
}

package features.context;

import features.Feature;
import features.context.impl.AuthorRankFeatureFactory;
import features.context.impl.LocalizedTime;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import lombok.experimental.Delegate;
import lombok.val;
import util.BasicUtils;
import util.IpGeoLocator;
import util.LocationToTimeZoneConverter;

/**
 * Create all features based on the metadata connected to the current revision
 */
class EditFeatureFactory {

  private final IpGeoLocator locator = new IpGeoLocator();
  private final LocationToTimeZoneConverter locationConverter = new LocationToTimeZoneConverter();

  @Delegate
  private final AuthorRankFeatureFactory authorRank = new AuthorRankFeatureFactory();

  /**
   * @return the hours since midnight
   */
  Feature timeOfDay() {
    return parameters -> {
      val ts = parameters.getRevision().getTimestamp();
      return BasicUtils.valid(ts.getHour());
    };
  }

  Feature localizedTimeOfDay() {
    return new LocalizedTime(ChronoField.HOUR_OF_DAY, locator, locationConverter);
  }

  Feature dayOfWeek() {
    return parameters -> {
      val ts = parameters.getRevision().getTimestamp();
      val day = BasicUtils.valid(ts.getDay());
      val month = BasicUtils.valid(ts.getMonth());
      val year = BasicUtils.valid(ts.getYear());
      return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    };
  }

  Feature localizedDayOfWeek() {
    return new LocalizedTime(ChronoField.DAY_OF_WEEK, locator, locationConverter);
  }

  Feature isMinorEdit() {
    return parameters -> parameters.getRevision().isMinor()
        ? 1 : 0;
  }

  // future feature currently not used
  Feature isCommentDeleted() {
    return parameters -> parameters.getRevision().getComment() != null
        && parameters.getRevision().getComment().getDeleted() != null
        ? 1 : 0;
  }

}

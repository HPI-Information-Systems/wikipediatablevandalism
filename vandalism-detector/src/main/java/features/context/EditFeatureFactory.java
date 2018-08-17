package features.context;

import features.Feature;
import features.context.impl.AuthorDiversity;
import features.context.impl.AuthorRank;
import features.context.impl.LocalizedTime;
import features.context.impl.RevisionProvider;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
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

  Feature authorRank() {
    return new AuthorRank(RevisionProvider.all());
  }

  Feature authorRankOfLast200Edits() {
    return new AuthorRank(RevisionProvider.lastN(200));
  }

  Feature authorRankOfLastMonth() {
    return new AuthorRank(RevisionProvider.maxAge(Period.ofDays(30)));
  }

  Feature authorRankOfLast200EditsOfOneMonth() {
    return new AuthorRank(RevisionProvider.lastNWithMaxAge(200, Period.ofDays(30)));
  }

  Feature distinctAuthorCountOfLast5Edits() {
    return new AuthorDiversity(RevisionProvider.lastN(5));
  }

  Feature distinctAuthorCountOfLast20Edits() {
    return new AuthorDiversity(RevisionProvider.lastN(20));
  }

  Feature distinctAuthorCountOfLast80Edits() {
    return new AuthorDiversity(RevisionProvider.lastN(80));
  }

  Feature distinctAuthorCountOfLastHour() {
    return new AuthorDiversity(RevisionProvider.maxAge(Duration.ofHours(1)));
  }

  Feature distinctAuthorCountOfLastTwoHours() {
    return new AuthorDiversity(RevisionProvider.maxAge(Duration.ofHours(2)));
  }

  Feature distinctAuthorCountOfLast24Hours() {
    return new AuthorDiversity(RevisionProvider.maxAge(Duration.ofDays(1)));
  }

}

package features.context;

import features.Feature;
import java.time.LocalDate;
import lombok.val;
import util.BasicUtils;

/**
 * Create all features based on the metadata connected to the current revision
 */
class EditFeatureFactory {

  /**
   * @return the hours since midnight
   */
  Feature timeOfDay() {
    return (revision, ignored) -> {
      val ts = revision.getTimestamp();
      return BasicUtils.valid(ts.getHour());
    };
  }

  Feature dayOfWeek() {
    return (revision, ignored) -> {
      val ts = revision.getTimestamp();
      val day = BasicUtils.valid(ts.getDay());
      val month = BasicUtils.valid(ts.getMonth());
      val year = BasicUtils.valid(ts.getYear());
      return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    };
  }

  Feature isMinorEdit() {
    return (revision, ignored) -> revision.isMinor();
  }

  // future feature currently not used
  Feature isCommentDeleted() {
    return (revision, featureContext) -> revision.getComment() != null
        && revision.getComment().getDeleted() != null;
  }

}

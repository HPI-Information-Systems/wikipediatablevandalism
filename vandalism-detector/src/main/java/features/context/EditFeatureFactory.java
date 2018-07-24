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
    return parameters -> {
      val ts = parameters.getRevision().getTimestamp();
      return BasicUtils.valid(ts.getHour());
    };
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

  Feature isMinorEdit() {
    return parameters -> parameters.getRevision().isMinor();
  }

  // future feature currently not used
  Feature isCommentDeleted() {
    return parameters -> parameters.getRevision().getComment() != null
        && parameters.getRevision().getComment().getDeleted() != null;
  }

}

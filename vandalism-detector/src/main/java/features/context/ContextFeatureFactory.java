package features.context;

import static features.context.Utils.valid;

import features.Feature;
import java.time.LocalDate;
import lombok.val;

/**
 * Create all features connected to the context of the actual revision.
 */
class ContextFeatureFactory {

  Feature isContributorAnonymous() {
    return (revision, ignored) -> revision.getContributor().getUsername() == null;
  }

  /**
   * @return the seconds since midnight
   */
  Feature timeOfDay() {
    return (revision, ignored) -> {
      val ts = revision.getTimestamp();
      return valid(ts.getHour());
    };
  }

  Feature dayOfWeek() {
    return (revision, ignored) -> {
      val ts = revision.getTimestamp();
      val day = valid(ts.getDay());
      val month = valid(ts.getMonth());
      val year = valid(ts.getYear());
      return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    };
  }

  Feature isMinorEdit() {
    return (revision, ignored) -> revision.isMinor();
  }

  Feature commentLength() {
    return (revision, ignored) -> {
      val comment = revision.getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }


}

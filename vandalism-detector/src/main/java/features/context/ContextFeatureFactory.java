package features.context;

import static features.context.Utils.valid;

import features.Feature;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import lombok.val;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Create all features connected to the context of the editing user.
 */
class ContextFeatureFactory {

  Feature isAnonymous() {
    return revision -> revision.getContributor().getUsername() == null;
  }

  Feature isContributorDeleted() {
    return revision -> revision.getContributor().getDeleted() != null;
  }

  /**
   * @return the seconds since midnight
   */
  Feature createdTimeOfDay() {
    return revision -> {
      val ts = revision.getTimestamp();
      val seconds = valid(ts.getSecond());
      val minutes = TimeUnit.SECONDS.convert(valid(ts.getMinute()), TimeUnit.MINUTES);
      val hours = TimeUnit.SECONDS.convert(valid(ts.getHour()), TimeUnit.HOURS);
      return hours + minutes + seconds;
    };
  }

  Feature createdDayOfWeek() {
    return revision -> {
      val ts = revision.getTimestamp();
      val day = valid(ts.getDay());
      val month = valid(ts.getMonth());
      val year = valid(ts.getYear());
      return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    };
  }

  Feature isMinorEdit() {
    return MyRevisionType::isMinor;
  }

  Feature commentLength() {
    return revision -> {
      val comment = revision.getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }

  Feature isCommentDeleted() {
    return revision -> revision.getComment() != null && revision.getComment().getDeleted() != null;
  }
}

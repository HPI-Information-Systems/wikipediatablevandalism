package features.context;

import features.Feature;
import java.time.LocalDate;
import lombok.val;
import util.BasicUtils;

/**
 * Create all features connected to the context of the actual revision.
 */
class ContextFeatureFactory {

  Feature isContributorAnonymous() {
    return parameters -> parameters.getRevision().getContributor().getUsername() == null;
  }

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

  Feature commentLength() {
    return parameters -> {
      val comment = parameters.getRevision().getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }

  Feature isBot() {
    val botList = BotList.read();
    return parameters -> {

      if (BasicUtils.isAnonymous(parameters.getRevision().getContributor())) {
        return false;
      }

      return botList.stream()
          .anyMatch(str -> str.equals(parameters.getRevision().getContributor().getUsername()));
    };
  }
}

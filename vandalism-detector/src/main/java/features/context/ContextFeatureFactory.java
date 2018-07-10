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
    return (revision, ignored) -> revision.getContributor().getUsername() == null;
  }

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

  Feature commentLength() {
    return (revision, ignored) -> {
      val comment = revision.getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }

  Feature isBot() {
    val botList = BotList.read();
    return (revision, ignored) -> {

      if (BasicUtils.isAnonymous(revision.getContributor())) {
        return false;
      }

      return botList.stream().anyMatch(str -> str.equals(revision.getContributor().getUsername()));
    };
  }
}

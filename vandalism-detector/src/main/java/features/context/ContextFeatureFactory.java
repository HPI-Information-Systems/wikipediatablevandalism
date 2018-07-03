package features.context;

import static features.context.Utils.valid;

import features.Feature;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import lombok.val;

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
    return (revision, ignored) -> TimeUnit.HOURS
        .convert(revision.getDate().getTime(), TimeUnit.MILLISECONDS);
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

  Feature isBot() {
    try {
      val botlist = Files
          .readAllLines(Paths.get(getClass().getClassLoader().getResource("botlist.txt").toURI()));
      return (revision, ignored) -> {
        if (!Utils.isAnonymous(revision.getContributor())) {
          return botlist.contains(revision.getContributor().getUsername());
        } else {
          return false;
        }
      };
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}

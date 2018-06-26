package features.context;

import com.google.common.collect.ImmutableMap;
import features.Feature;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.xml.datatype.DatatypeConstants;
import lombok.val;
import wikixmlsplit.datastructures.MyRevisionType;

public class ContextFeatures {

  public Map<String, Feature> features() {
    return ImmutableMap.<String, Feature>builder()
        .put("anonym", isAnonymous())
        .put("deleted", isDeleted())
        .put("tod", createdTimeOfDay())
        .put("dow", createdDayOfWeek())
        .put("minor", isMinor())
        .put("commentLength", commentLength())
        .put("commentDeleted", commentDeleted())
        .build();
  }

  private Feature isAnonymous() {
    return revision -> revision.getContributor().getUsername() == null;
  }

  private Feature isDeleted() {
    return revision -> revision.getContributor().getDeleted() != null;
  }

  /**
   * @return the seconds since midnight
   */
  private Feature createdTimeOfDay() {
    return revision -> {
      val ts = revision.getTimestamp();
      val seconds = valid(ts.getSecond());
      val minutes = TimeUnit.SECONDS.convert(valid(ts.getMinute()), TimeUnit.MINUTES);
      val hours = TimeUnit.SECONDS.convert(valid(ts.getHour()), TimeUnit.HOURS);
      return hours + minutes + seconds;
    };
  }

  private Feature createdDayOfWeek() {
    return revision -> {
      val ts = revision.getTimestamp();
      val day = valid(ts.getDay());
      val month = valid(ts.getMonth());
      val year = valid(ts.getYear());
      return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    };
  }

  private Feature isMinor() {
    return MyRevisionType::isMinor;
  }

  private Feature commentLength() {
    return revision -> {
      val comment = revision.getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }

  private Feature commentDeleted() {
    return revision -> revision.getComment() != null && revision.getComment().getDeleted() != null;
  }

  private int valid(final int value) {
    if (value == DatatypeConstants.FIELD_UNDEFINED) {
      throw new IllegalArgumentException("field undefined");
    }
    return value;
  }
}

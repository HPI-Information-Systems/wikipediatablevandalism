package features.context;

import java.util.List;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.val;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;

import java.util.concurrent.TimeUnit;
import wikixmlsplit.datastructures.MyRevisionType;

class Utils {

  /**
   * Check if return values of {@link javax.xml.datatype.XMLGregorianCalendar} are valid, else
   * throw.
   *
   * @param value the value to check
   * @return the valid value
   */
  static int valid(final int value) {
    if (value == DatatypeConstants.FIELD_UNDEFINED) {
      throw new IllegalArgumentException("field undefined");
    }
    return value;
  }

  static boolean isAnonymous(final ContributorType contributor) {
    return contributor.getUsername() == null;
  }

  static long totalTime(XMLGregorianCalendar timestamp) {
    val seconds = valid(timestamp.getSecond());
    val minutes = TimeUnit.SECONDS.convert(valid(timestamp.getMinute()), TimeUnit.MINUTES);
    val hours = TimeUnit.SECONDS.convert(valid(timestamp.getHour()), TimeUnit.HOURS);
    val days = TimeUnit.SECONDS.convert(valid(timestamp.getDay()), TimeUnit.DAYS);
    val month = TimeUnit.SECONDS.convert(valid(timestamp.getMonth()), TimeUnit.DAYS);
    val year = TimeUnit.SECONDS.convert(valid(timestamp.getYear()), TimeUnit.DAYS);
    return seconds + minutes + hours + days + month + year;
  }

  static int parsedLength(List<String> parsed) {
    if (parsed != null) {
      int totalLength = 0;
      for (String string: parsed) {
        totalLength += string.length();
      }
      return totalLength;
    } else {
      return 0;
    }
  }

  static MyRevisionType getPreviousRevision(List<MyRevisionType> previousRevisions) {
    if (previousRevisions != null) {
      if (previousRevisions.size() > 0) {
        return previousRevisions.get(0);
      }
    }
    return null;
  }
}
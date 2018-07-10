package features.context;

import java.util.List;
import javax.xml.datatype.DatatypeConstants;

import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;

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

  static int parsedLength(List<String> parsed) {
    if (parsed == null) {
      return 0;
    }
    int totalLength = 0;
    for (String string : parsed) {
      totalLength += string.length();
    }
    return totalLength;
  }

  static MyRevisionType getPreviousRevision(List<MyRevisionType> previousRevisions) {
    if (previousRevisions == null || previousRevisions.size() == 0) {
      return null;
    }
    return previousRevisions.get(0);
  }

  static boolean hasSameContributor(MyRevisionType revision1, MyRevisionType revision2) {
    return (Utils.isAnonymous(revision1.getContributor()) &&
        Utils.isAnonymous(revision2.getContributor()) &&
        revision1.getContributor().getIp().equals(revision2.getContributor().getIp()))
        ||
        (!Utils.isAnonymous(revision1.getContributor()) &&
        !Utils.isAnonymous(revision2.getContributor()) &&
        revision1.getContributor().getId().equals(revision2.getContributor().getId()));
  }
}
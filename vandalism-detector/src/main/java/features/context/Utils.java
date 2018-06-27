package features.context;

import javax.xml.datatype.DatatypeConstants;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;

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
}
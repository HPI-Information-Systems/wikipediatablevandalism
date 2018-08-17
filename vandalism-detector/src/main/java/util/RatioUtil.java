package util;

public class RatioUtil {

  /**
   * Compute the added ratio X: {@code prev * X = current}.
   *
   * Value in between [1, factor], given that the previous count was not zero. In case that previous
   * count was zero, take the number of added items as factor.
   */
  public static double added(final double previous, final double current) {
    // Currently empty - cannot possibly have added something
    if (current == 0) {
      return 0;
    }

    // Currently not empty and previously empty - all added
    if (previous == 0) {
      return current;
    }

    // Count increased
    if (previous < current) {
      double added = current - previous;
      return 1 + added / previous;
    }

    // Count decreased
    return 0;
  }

  /**
   * Compute the removed ratio X {@code previous * X = current}. Values in [1, 0]. A value of one
   * encodes no deletions (including same amount or additions), and zero encodes blanking.
   */
  public static double removed(final double previous, final double current) {
    // Previously empty - cannot remove anything
    if (previous == 0) {
      return 0;
    }

    // Compute ratio, set additions to zero
    double removedRefCount = previous - current;
    return Math.max(0, removedRefCount) / previous;
  }
}

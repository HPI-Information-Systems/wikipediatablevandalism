package features;

import java.util.List;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Features which compare two input revisions and compute some kind of delta.
 */
@FunctionalInterface
public interface DeltaFeature {

  /**
   * Assess a revision with respect its precursors (revisions of the same article that appeared
   * before the current revision).
   *
   * @param precursors precursor revision: ordered in descending order by time; the first element is
   * the most recent precursor
   * @param revision the revision to assess
   */
  Object getValue(List<MyRevisionType> precursors, MyRevisionType revision);

}

package features;

import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Features which operate on a single input revision.
 */
@FunctionalInterface
public interface Feature {

  Object getValue(MyRevisionType revision);
}

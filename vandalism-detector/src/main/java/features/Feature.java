package features;

import wikixmlsplit.datastructures.MyRevisionType;

@FunctionalInterface
public interface Feature {

  Object getValue(MyRevisionType revision);
}

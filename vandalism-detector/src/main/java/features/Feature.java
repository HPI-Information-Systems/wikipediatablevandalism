package features;

import wikixmlsplit.datastructures.MyRevisionType;

public interface Feature {

  Object getValue(MyRevisionType revision);
}

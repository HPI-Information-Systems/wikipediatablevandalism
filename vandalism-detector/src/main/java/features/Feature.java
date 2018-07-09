package features;

import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * all Features which operate on the context of the revision.
 */
@FunctionalInterface
public interface Feature {

  Object getValue(MyRevisionType revision, FeatureContext featureContext);
}

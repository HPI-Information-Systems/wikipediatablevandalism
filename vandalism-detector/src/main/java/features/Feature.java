package features;

import model.FeatureParameters;

/**
 * all Features which operate on the context of the revision.
 */
@FunctionalInterface
public interface Feature {

  Object getValue(FeatureParameters parameters);
}

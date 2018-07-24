package features.content.util.language;

import static util.FeatureUtil.averageOfFeatures;

import features.Feature;
import java.util.List;
import model.FeatureParameters;

public class AverageFeature implements Feature {

  private final List<Feature> features;

  public AverageFeature(List<Feature> features) {
    this.features = features;
  }

  @Override
  public double getValue(FeatureParameters parameters) {
    return averageOfFeatures(parameters, this.features);
  }
}

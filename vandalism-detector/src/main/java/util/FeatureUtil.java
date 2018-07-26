package util;

import features.Feature;
import java.util.List;
import model.FeatureParameters;

public class FeatureUtil {

  public static double averageOfFeatures(FeatureParameters parameters, List<Feature> features) {
    return features.stream()
        .mapToDouble(feature -> feature.getValue(parameters))
        .average()
        .orElse(0);
  }
}

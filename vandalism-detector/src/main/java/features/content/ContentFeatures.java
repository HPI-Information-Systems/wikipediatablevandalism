package features.content;

import features.FeaturePack;
import lombok.val;
import matching.MatchService;

public class ContentFeatures {

  private static final ContentFeatures INSTANCE = new ContentFeatures();

  private final FeaturePack features;

  private ContentFeatures() {
    val factory = new ContentFeatureFactory(new MatchService());

    features = FeaturePack.builder()
        .feature("tableGeometryChange", factory.geometryChange())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContentFeatures get() {
    return INSTANCE;
  }
}
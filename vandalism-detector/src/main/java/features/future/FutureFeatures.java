package features.future;

import features.FeaturePack;
import lombok.val;

public class FutureFeatures {

  private static final FutureFeatures INSTANCE = new FutureFeatures();

  private final FeaturePack features;

  private FutureFeatures() {
    val factory = new FutureFeatureFactory();

    features = FeaturePack.builder()
        .feature("isCommentDeleted", factory.isCommentDeleted())
        .feature("isContributorDeleted", factory.isContributorDeleted())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static FutureFeatures get() {
    return INSTANCE;
  }
}

package features.content;

import features.FeaturePack;
import lombok.val;

public class ContentFeatures {

  private static final ContentFeatures INSTANCE = new ContentFeatures();

  private final FeaturePack features;

  private ContentFeatures() {
    val factory = new ContentFeatureFactory();

    features = FeaturePack.builder()
        .feature("cellCountChange", factory.cellCount())
        .feature("rowCountChange", factory.rowCount())
        .feature("columnCountChange", factory.columnCount())
        .feature("sharedCellRatio", factory.sharedCellRatio())
        .feature("isRankChange", factory.rankChange())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContentFeatures get() {
    return INSTANCE;
  }
}

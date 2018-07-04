package features.basic;

import features.FeaturePack;
import lombok.val;

public class BasicFeatures {

  private static final BasicFeatures INSTANCE = new BasicFeatures();

  private final FeaturePack features;

  private BasicFeatures() {
    val factory = new BasicFeatureFactory();

    features = FeaturePack.builder()
        .feature("currentRowCount", factory::rowCount)
        .feature("currentColumnCount", factory::columnCount)
        .feature("currentCellCount", factory::cellCount)
        .feature("unmatchedTableRatio", factory::unmatchedTables)
        .feature("unmatchedRowRatio", factory::unmatchedRows)
        .build();
  }


  public FeaturePack getFeatures() {
    return features;
  }

  public static BasicFeatures get() {
    return INSTANCE;
  }
}

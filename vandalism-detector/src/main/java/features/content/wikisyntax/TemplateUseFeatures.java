package features.content.wikisyntax;

import features.FeaturePack;
import lombok.val;

public class TemplateUseFeatures {

  private static final TemplateUseFeatures INSTANCE = new TemplateUseFeatures();

  private final FeaturePack features;

  private TemplateUseFeatures() {
    val factory = new TemplateUseFeatureFactory();

    features = FeaturePack.builder()
        .feature("templateUseFlags", factory.flagChanges())
        .feature("templateUseFifaFlags", factory.fifaFlagChanges())
        .feature("templateUsePageLink", factory.pageLinkChanges())
        .feature("templateUseYesNo", factory.yesNoChanges())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static TemplateUseFeatures get() {
    return INSTANCE;
  }
}

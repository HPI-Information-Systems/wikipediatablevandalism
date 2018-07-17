package features.language;

import features.FeaturePack;
import lombok.val;

public class LanguageFeatures {

  private static final LanguageFeatures INSTANCE = new LanguageFeatures();

  private final FeaturePack features;

  private LanguageFeatures() {
    val factory = new LanguageFeatureFactory();

    features = FeaturePack.builder()
        .feature("offensiveWordCountInTables", factory.offensiveWordsInTable())
        .feature("offensiveWordCountInComment", factory.offensiveWordsInComment())
        .feature("addedNonDictionaryWordCount", factory.addedNonDictionaryWordCount())
        .feature("pronounsInComment", factory.pronounsInComment())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static LanguageFeatures get() {
    return INSTANCE;
  }
}

package features.language;

import features.FeaturePack;
import lombok.val;

public class LanguageFeatures {

  private static final LanguageFeatures INSTANCE = new LanguageFeatures();

  private final FeaturePack features;

  private LanguageFeatures() {
    val factory = new LanguageFeatureFactory();

    features = FeaturePack.builder()
        .feature("offensiveWordFrequencyInTables", factory.offensiveWordFrequencyInTables())
        .feature("offensiveWordImpactInTables", factory.offensiveWordImpactInTables())
        .feature("offensiveWordFrequencyInComment", factory.offensiveWordFrequencyInComment())
        .feature("personalPronounFrequencyInComment", factory.personalPronounFrequencyInComment())
        .feature("personalPronounFrequencyInTable", factory.personalPronounFrequencyInTable())
        .feature("personalPronounImpactInTable", factory.personalPronounImpactInTable())
        .feature("nonDictionaryWordFrequencyInTable", factory.nonDictionaryWordFrequencyInTable())
        .feature("nonDictionaryWordImpactInTable", factory.nonDictionaryWordImpactInTable())
        .feature("nonDictionaryWordFrequencyInComment", factory.nonDictionaryWordFrequencyInComment())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static LanguageFeatures get() {
    return INSTANCE;
  }
}

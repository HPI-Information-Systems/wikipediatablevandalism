package features.language;

import features.FeaturePack;
import lombok.val;

public class LanguageFeatures {

  private static final LanguageFeatures INSTANCE = new LanguageFeatures();

  private final FeaturePack features;

  private LanguageFeatures() {
    val factory = new LanguageFeatureFactory();

    features = FeaturePack.builder()
        .feature("personalPronounFrequencyInComment", factory.personalPronounFrequencyInComment())
        .feature("personalPronounFrequencyInTable", factory.personalPronounFrequencyInTable())
        .feature("personalPronounImpactInTable", factory.personalPronounImpactInTable())
        .feature("vulgarWordFrequencyInTables", factory.vulgarWordFrequencyInTables())
        .feature("vulgarWordImpactInTables", factory.vulgarWordImpactInTables())
        .feature("vulgarWordFrequencyInComment", factory.vulgarWordFrequencyInComment())
        .feature("sexualWordFrequencyInTables", factory.sexualWordFrequencyInTables())
        .feature("sexualWordImpactInTables", factory.sexualWordImpactInTables())
        .feature("sexualWordFrequencyInComment", factory.sexualWordFrequencyInComment())
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

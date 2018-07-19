package features.language;

import features.FeaturePack;
import lombok.val;

public class LanguageFeatures {

  private static final LanguageFeatures INSTANCE = new LanguageFeatures();

  private final FeaturePack features;

  private LanguageFeatures() {
    val factory = new LanguageFeatureFactory();

    features = FeaturePack.builder()
        .feature("offensiveWordFrequencyInTables", factory.tableOffensiveWordFrequency())
        .feature("offensiveWordImpactInTables", factory.tableOffensiveWordImpact())
        .feature("offensiveWordFrequencyInComment", factory.commentOffensiveWordFrequency())
        .feature("personalPronounFrequencyInComment", factory.commentPersonalPronounFrequency())
        .feature("personalPronounFrequencyInTable", factory.tablePersonalPronounFrequency())
        .feature("personalPronounImpactInTable", factory.tablePersonalPronounImpact())
        .feature("nonDictionaryWordFrequencyInTable", factory.tableNonDictionaryWordFrequency())
        .feature("nonDictionaryWordImpactInTable", factory.tableNonDictionaryWordImpact())
        .feature("nonDictionaryWordFrequencyInComment", factory.commentNonDictionaryWordFrequency())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static LanguageFeatures get() {
    return INSTANCE;
  }
}

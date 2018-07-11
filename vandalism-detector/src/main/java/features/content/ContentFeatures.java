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
        .feature("ratioOfNumericalCharsToAllChars", factory.ratioOfNumericalCharsToAllChars())
        .feature("ratioOfAlphanumericCharsToAllChars", factory.ratioOfAlphanumericCharsToAllChars())
        .feature("ratioOfUppercaseCharsToAllChars", factory.ratioOfUppercaseCharsToAllChars())
        .feature("ratioOfUppercaseCharsToLowercaseChars",
            factory.ratioOfUppercaseCharsToLowercaseChars())
        .feature("lengthOfLongestConsecutiveSequenceOfSingleChar",
            factory.lengthOfLongestConsecutiveSequenceOfSingleChar())
        .feature("lengthOfLongestToken", factory.lengthOfLongestToken())
        .feature("previousLength", factory.previousLength())
        .feature("offensiveWordCountInTables", factory.offensiveWordsInTable())
        .feature("offensiveWordCountInComment", factory.offensiveWordsInComment())
        .feature("averageRelativeFrequencyOfNewAddedWords", factory.averageRelativeFrequencyOfNewAddedWords())
        .feature("LZWCompressionRate", factory.LZWCompressionRate())
        .feature("KLDOfCharDistribution", factory.KLDOfCharDistribution())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContentFeatures get() {
    return INSTANCE;
  }
}

package features.content;

import features.FeaturePack;
import lombok.val;

public class ContentFeatures {

  private static final ContentFeatures INSTANCE = new ContentFeatures();

  private final FeaturePack features;

  private ContentFeatures() {
    val tableFeatureFactory = new TableFeatureFactory();
    val languageFeatureFactory = new LanguageFeatureFactory();
    val textFeatureFactory = new TextFeatureFactory();
    val byteFeatureFactory = new ByteFeatureFactory();

    features = FeaturePack.builder()
        .feature("currentRowCount", tableFeatureFactory.currentRowCount())
        .feature("currentColumnCount", tableFeatureFactory.currentColumnCount())
        .feature("currentCellCount", tableFeatureFactory.currentCellCount())
        .feature("unmatchedTableRatio", tableFeatureFactory.unmatchedTables())
        .feature("unmatchedRowRatio", tableFeatureFactory.unmatchedRows())
        .feature("cellCountChange", tableFeatureFactory.cellCount())
        .feature("rowCountChange", tableFeatureFactory.rowCount())
        .feature("columnCountChange", tableFeatureFactory.columnCount())
        .feature("sharedCellRatio", tableFeatureFactory.sharedCellRatio())
        .feature("isRankChange", tableFeatureFactory.rankChange())
        .feature("offensiveWordCountInTables", languageFeatureFactory.offensiveWordsInTable())
        .feature("offensiveWordCountInComment", languageFeatureFactory.offensiveWordsInComment())
        .feature("addedNonDictionaryWordCount", languageFeatureFactory.addedNonDictionaryWordCount())
        .feature("ratioOfNumericalCharsToAllChars", textFeatureFactory.ratioOfNumericalCharsToAllChars())
        .feature("ratioOfAlphanumericCharsToAllChars", textFeatureFactory.ratioOfAlphanumericCharsToAllChars())
        .feature("ratioOfUppercaseCharsToAllChars", textFeatureFactory.ratioOfUppercaseCharsToAllChars())
        .feature("ratioOfUppercaseCharsToLowercaseChars", textFeatureFactory.ratioOfUppercaseCharsToLowercaseChars())
        .feature("lengthOfLongestConsecutiveSequenceOfSingleChar", textFeatureFactory.lengthOfLongestConsecutiveSequenceOfSingleChar())
        .feature("lengthOfLongestToken", textFeatureFactory.lengthOfLongestToken())
        .feature("averageRelativeFrequencyOfNewAddedWords", textFeatureFactory.averageRelativeFrequencyOfNewAddedWords())
        .feature("previousLength", byteFeatureFactory.previousLength())
        .feature("sizeChange", byteFeatureFactory.sizeChange())
        .feature("LZWCompressionRate", byteFeatureFactory.LZWCompressionRate())
        .feature("KLDOfCharDistribution", byteFeatureFactory.KLDOfCharDistribution())
        .feature("commentLength", byteFeatureFactory.commentLength())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContentFeatures get() {
    return INSTANCE;
  }
}

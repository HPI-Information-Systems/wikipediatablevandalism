package features.content;

import features.FeaturePack;
import features.content.util.typing.DataTypeDependentFeatureFactory;
import features.content.wikisyntax.TemplateUseFeatures;
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
        // Table
        .feature("currentRowCount", tableFeatureFactory.currentRowCount())
        .feature("currentColumnCount", tableFeatureFactory.currentColumnCount())
        .feature("currentCellCount", tableFeatureFactory.currentCellCount())
        .feature("unmatchedTableRatio", tableFeatureFactory.unmatchedTables())
        .feature("unmatchedRowRatio", tableFeatureFactory.unmatchedRows())
        .feature("cellCountChange", tableFeatureFactory.cellCount())
        .feature("rowCountChange", tableFeatureFactory.rowCount())
        .feature("columnCountChange", tableFeatureFactory.columnCount())
        .feature("sharedCellRatio", tableFeatureFactory.sharedCellRatio())
        .feature("rankChange", tableFeatureFactory.rankChange())
        //.feature("tableClipCount", tableFeatureFactory.tableClipCount())
        .feature("syntaxCount", tableFeatureFactory.openAndCloseSyntaxCount())
        .feature("sizePerCellChangeRatio", tableFeatureFactory.sizePerCellChangeRatio())
        .feature("sizePerCell", tableFeatureFactory.sizePerCell())
        .feature("emptyCellChangeRatio", tableFeatureFactory.emptyCellRatio())
        .feature("emptyCellCount", tableFeatureFactory.emptyCellCount())
        .feature("addedInvalidAttributes", tableFeatureFactory.addedInvalidAttributes())

        // Language
        .feature("personalPronounFrequencyInComment", languageFeatureFactory.personalPronounFrequencyInComment())
        .feature("personalPronounFrequencyInTable", languageFeatureFactory.personalPronounFrequencyInTable())
        .feature("personalPronounImpactInTable", languageFeatureFactory.personalPronounImpactInTable())
        .feature("vulgarWordFrequencyInComment", languageFeatureFactory.vulgarWordFrequencyInComment())
        .feature("vulgarWordFrequencyInTables", languageFeatureFactory.vulgarWordFrequencyInTables())
        .feature("vulgarWordImpactInTables", languageFeatureFactory.vulgarWordImpactInTables())
        .feature("sexualWordFrequencyInComment", languageFeatureFactory.sexualWordFrequencyInComment())
        .feature("sexualWordFrequencyInTables", languageFeatureFactory.sexualWordFrequencyInTables())
        .feature("sexualWordImpactInTables", languageFeatureFactory.sexualWordImpactInTables())
        .feature("nonDictionaryWordFrequencyInComment", languageFeatureFactory.nonDictionaryWordFrequencyInComment())
        .feature("nonDictionaryWordFrequencyInTable", languageFeatureFactory.nonDictionaryWordFrequencyInTable())
        .feature("nonDictionaryWordImpactInTable", languageFeatureFactory.nonDictionaryWordImpactInTable())
        .feature("superlativeWordFrequencyInTable", languageFeatureFactory.superlativeWordFrequencyInTable())
        .feature("superlativeWordImpactInTable", languageFeatureFactory.superlativeWordImpactInTable())
        .feature("superlativeWordFrequencyInComment", languageFeatureFactory.superlativeWordFrequencyInComment())
        .feature("wikiSyntaxElementFrequencyInTable", languageFeatureFactory.wikiSyntaxElementFrequencyInTable())
        .feature("wikiSyntaxElementImpactInTable", languageFeatureFactory.wikiSyntaxElementImpactInTable())
        .feature("wikiSyntaxElementFrequencyInComment", languageFeatureFactory.wikiSyntaxElementFrequencyInComment())
        .feature("averageAllBadWordFrequencyInTable", languageFeatureFactory.averageAllBadWordFrequencyInTable())
        .feature("averageAllBadWordImpactInTable", languageFeatureFactory.averageAllBadWordImpactInTable())
        .feature("averageAllBadWordFrequencyInComment", languageFeatureFactory.averageAllBadWordFrequencyInComment())
        .feature("revertInComment", languageFeatureFactory.revertInComment())
        .feature("redirectInComment", languageFeatureFactory.redirectInComment())
        .feature("replaceInComment", languageFeatureFactory.replaceInComment())
        .feature("goodFaithInComment", languageFeatureFactory.goodFaithInComment())

        // Text
        .feature("digitRatio", textFeatureFactory.ratioOfNumericalCharsToAllChars())
        .feature("alphanumericRatio", textFeatureFactory.ratioOfAlphanumericCharsToAllChars())
        .feature("uppercaseRatio", textFeatureFactory.ratioOfUppercaseCharsToAllChars())
        .feature("uppercaseToLowercaseRatio", textFeatureFactory.ratioOfUppercaseCharsToLowercaseChars())
        .feature("longCharSeq", textFeatureFactory.lengthOfLongestConsecutiveSequenceOfSingleChar())
        .feature("lengthOfLongestToken", textFeatureFactory.lengthOfLongestToken())
        .feature("newWordFrequency", textFeatureFactory.newWordFrequency())
        .feature("refChangeRatio", textFeatureFactory.refChangeRatio())
        .feature("refCount", textFeatureFactory.refCount())

        // Byte
        .feature("previousLength", byteFeatureFactory.previousLength())
        .feature("sizeChange", byteFeatureFactory.sizeChange())
        .feature("sizeRatio", byteFeatureFactory.sizeRatio())
        .feature("LZWCompressionRate", byteFeatureFactory.LZWCompressionRate())
        .feature("KLDOfCharDistribution", byteFeatureFactory.KLDOfCharDistribution())
        .feature("rawCommentLength", byteFeatureFactory.rawCommentLength())
        .feature("userCommentLength", byteFeatureFactory.userCommentLength())

        .feature("hasNumericOutlierInColumns", tableFeatureFactory.hasNumericOutlierInColumns())
        .feature("hasNumericOutlierInRows", tableFeatureFactory.hasNumericOutlierInRows())
        .feature("tableDataTypeInformationGain", new DataTypeDependentFeatureFactory().dataTypeDistributionInformationGain())

        .build()
        .combineWith(TemplateUseFeatures.get().getFeatures());
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContentFeatures get() {
    return INSTANCE;
  }
}

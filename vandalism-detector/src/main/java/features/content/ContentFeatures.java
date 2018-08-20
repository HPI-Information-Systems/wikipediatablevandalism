package features.content;

import features.FeaturePack;
import features.content.wikisyntax.TemplateUseFeatures;
import lombok.val;

public class ContentFeatures {

  private static final ContentFeatures INSTANCE = new ContentFeatures();

  private final FeaturePack features;

  private ContentFeatures() {
    val table = new TableFeatureFactory();
    val language = new LanguageFeatureFactory();
    val text = new TextFeatureFactory();
    val bytes = new ByteFeatureFactory();

    features = FeaturePack.builder()
        // Table
        .feature("currentRowCount", table.currentRowCount())
        .feature("currentColumnCount", table.currentColumnCount())
        .feature("currentCellCount", table.currentCellCount())
        .feature("unmatchedTableRatio", table.unmatchedTableRatio())
        .feature("unmatchedRowRatio", table.unmatchedRowRatio())
        .feature("cellCountChange", table.cellCount())
        .feature("rowCountChange", table.rowCount())
        .feature("columnCountChange", table.columnCount())
        .feature("sharedCellRatio", table.sharedCellRatio())
        .feature("rankChange", table.rankChange())
        //.feature("tableClipCount", tableFeatureFactory.tableClipCount())
        //.feature("syntaxCount", tableFeatureFactory.openAndCloseSyntaxCount())
        .feature("averageCellSizeIncrease", table.averageCellSizeIncrease())
        .feature("averageCellSizeDecrease", table.averageCellSizeDecrease())
        .feature("averageCellSize", table.averageCellSize())
        .feature("addedEmptyCellRatio", table.addedEmptyCellRatio())
        .feature("removedEmptyCellRatio", table.removedEmptyCellRatio())
        .feature("emptyCellCount", table.emptyCellCount())
        .feature("addedInvalidAttributes", table.addedInvalidAttributes())

        .feature("isTableAdded", table.isTableAdded())
        .feature("addedTableCount", table.addedTableCount())
        .feature("isTableModified", table.isTableModified())
        .feature("modifiedTableCount", table.modifiedTableCount())
        .feature("isTableDeleted", table.isTableDeleted())
        .feature("deletedTableCount", table.deletedTableCount())
        .feature("areMultipleTablesChanged", table.areMultipleTablesChanged())
        .feature("isTableReplacement", table.isTableReplacement())

        .feature("hasNumericOutlierInColumns", table.hasNumericOutlierInColumns())
        .feature("hasNumericOutlierInRows", table.hasNumericOutlierInRows())
        .feature("hasNumericOutlierInChangedCellValues",
            table.hasNumericOutlierInChangedCellValues())
        .feature("tableDataTypeInformationGain", table.dataTypeDistributionInformationGain())

        // Language
        .feature("personalPronounInComment", language.personalPronounInComment())
        .feature("personalPronounFrequencyInComment", language.personalPronounFrequencyInComment())
        .feature("personalPronounFrequencyInTable", language.personalPronounFrequencyInTable())
        .feature("personalPronounImpactInTable", language.personalPronounImpactInTable())

        .feature("singularPersonalPronounInComment", language.singularPersonalPronounInComment())
        .feature("singularPersonalPronounFrequencyInComment",
            language.singularPersonalPronounFrequencyInComment())
        .feature("singularPersonalPronounFrequencyInTable",
            language.singularPersonalPronounFrequencyInTable())
        .feature("singularPersonalPronounImpactInTable",
            language.singularPersonalPronounImpactInTable())

        .feature("vulgarWordFrequencyInComment", language.vulgarWordFrequencyInComment())
        .feature("vulgarWordFrequencyInTables", language.vulgarWordFrequencyInTables())
        .feature("vulgarWordImpactInTables", language.vulgarWordImpactInTables())
        .feature("sexualWordFrequencyInComment", language.sexualWordFrequencyInComment())
        .feature("sexualWordFrequencyInTables", language.sexualWordFrequencyInTables())
        .feature("sexualWordImpactInTables", language.sexualWordImpactInTables())
        .feature("nonDictionaryWordFrequencyInComment",
            language.nonDictionaryWordFrequencyInComment())
        .feature("nonDictionaryWordFrequencyInTable", language.nonDictionaryWordFrequencyInTable())
        .feature("nonDictionaryWordImpactInTable", language.nonDictionaryWordImpactInTable())
        .feature("superlativeWordFrequencyInTable", language.superlativeWordFrequencyInTable())
        .feature("superlativeWordImpactInTable", language.superlativeWordImpactInTable())
        .feature("superlativeWordFrequencyInComment", language.superlativeWordFrequencyInComment())
        .feature("wikiSyntaxElementFrequencyInTable", language.wikiSyntaxElementFrequencyInTable())
        .feature("wikiSyntaxElementImpactInTable", language.wikiSyntaxElementImpactInTable())
        .feature("wikiSyntaxElementFrequencyInComment",
            language.wikiSyntaxElementFrequencyInComment())
        .feature("averageAllBadWordFrequencyInTable", language.averageAllBadWordFrequencyInTable())
        .feature("averageAllBadWordImpactInTable", language.averageAllBadWordImpactInTable())
        .feature("averageAllBadWordFrequencyInComment",
            language.averageAllBadWordFrequencyInComment())
        .feature("revertInComment", language.revertInComment())
        .feature("redirectInComment", language.redirectInComment())
        .feature("replaceInComment", language.replaceInComment())
        .feature("goodFaithInComment", language.goodFaithInComment())

        // Text
        .feature("digitRatio", text.ratioOfNumericalCharsToAllChars())
        .feature("alphanumericRatio", text.ratioOfAlphanumericCharsToAllChars())
        .feature("uppercaseRatio", text.ratioOfUppercaseCharsToAllChars())
        .feature("uppercaseToLowercaseRatio", text.ratioOfUppercaseCharsToLowercaseChars())
        .feature("longCharSeq", text.lengthOfLongestConsecutiveSequenceOfSingleChar())
        .feature("lengthOfLongestToken", text.lengthOfLongestToken())
        .feature("newWordFrequency", text.newWordFrequency())
        .feature("refAddedRatio", text.refAddedRatio())
        .feature("refRemovedRatio", text.refRemovedRatio())
        .feature("refCount", text.refCount())

        // Byte
        .feature("previousLength", bytes.previousLength())
        .feature("sizeChange", bytes.sizeChange())
        .feature("addedSizeRatio", bytes.addSizeRatio())
        .feature("removedSizeRatio", bytes.removedSizeRatio())
        .feature("LZWCompressionRate", bytes.LZWCompressionRate())
        .feature("kldOfAddedCharDistribution", bytes.kldOfAddedCharDistribution())
        .feature("kldOfCharDistribution", bytes.kldOfCharDistribution())
        .feature("rawCommentLength", bytes.rawCommentLength())
        .feature("userCommentLength", bytes.userCommentLength())

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

package features.content;

import com.google.common.collect.Multisets;
import features.Feature;
import features.content.TableGeometry.Measure;
import lombok.RequiredArgsConstructor;
import lombok.val;
import util.BasicUtils;
import util.KLDUtil;
import util.TableContentExtractor;
import util.WordsExtractor;
import util.ZipUtil;

@RequiredArgsConstructor
class ContentFeatureFactory {

  Feature cellCount() {
    return new TableGeometry(Measure.Product);
  }

  Feature columnCount() {
    return new TableGeometry(Measure.Columns);
  }

  Feature rowCount() {
    return new TableGeometry(Measure.Rows);
  }

  Feature sharedCellRatio() {
    return new SharedCellRatio();
  }

  Feature rankChange() {
    return new RankChange();
  }

  Feature offensiveWordsInComment() {
    return new OffensiveWordsInComment();
  }

  Feature offensiveWordsInTable() {
    return new OffensiveWordsInTable();
  }

  Feature ratioOfNumericalCharsToAllChars() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      float numericalCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isDigit(c)) {
          ++numericalCount;
        }
      }
      return numericalCount / (float) tableContents.length();
    };
  }

  Feature ratioOfAlphanumericCharsToAllChars() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      float alphanumericalCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isAlphabetic(c) || Character.isDigit(c)) {
          ++alphanumericalCount;
        }
      }
      return alphanumericalCount / (float) tableContents.length();
    };
  }

  Feature ratioOfUppercaseCharsToAllChars() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      float uppercaseCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isUpperCase(c)) {
          ++uppercaseCount;
        }
      }
      return uppercaseCount / (float) tableContents.length();
    };
  }

  Feature ratioOfUppercaseCharsToLowercaseChars() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      float uppercaseCount = 0;
      float lowercaseCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isUpperCase(c)) {
          ++uppercaseCount;
        } else if (Character.isLowerCase(c)) {
          ++lowercaseCount;
        }
      }

      if (lowercaseCount == 0) {
        return 1;
      }

      return uppercaseCount / lowercaseCount;
    };
  }

  Feature lengthOfLongestConsecutiveSequenceOfSingleChar() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      char charBefore = ' ';
      int longestConsecutiveSingleCharCount = 0;
      int currentConsecutiveSingleCharCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isWhitespace(c)) {
          continue;
        }
        if (charBefore != c) {
          if (currentConsecutiveSingleCharCount > longestConsecutiveSingleCharCount) {
            longestConsecutiveSingleCharCount = currentConsecutiveSingleCharCount;
          }
          currentConsecutiveSingleCharCount = 1;
          charBefore = c;
        } else {
          ++currentConsecutiveSingleCharCount;
        }
      }
      if (currentConsecutiveSingleCharCount
          > longestConsecutiveSingleCharCount) { // last consecutiveSingleCharSequence could be longest
        longestConsecutiveSingleCharCount = currentConsecutiveSingleCharCount;
      }
      return longestConsecutiveSingleCharCount;
    };
  }

  Feature lengthOfLongestToken() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      int longestTokenCount = 0;
      int currentTokenCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isWhitespace(c)) {
          if (currentTokenCount > longestTokenCount) {
            longestTokenCount = currentTokenCount;
          }
          currentTokenCount = 0;
        } else {
          ++currentTokenCount;
        }
      }
      if (currentTokenCount > longestTokenCount) { // last token could be longest
        longestTokenCount = currentTokenCount;
      }
      return longestTokenCount;
    };
  }

  Feature previousLength() {
    return (ignored, featureContext) -> {
      val previousRevision = BasicUtils
          .getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision == null) {
        return 0;
      }
      return TableContentExtractor.getContent(previousRevision).length();
    };
  }

  Feature averageRelativeFrequencyOfNewAddedWords() { // FIXME to test with deletion corpus
    return (revision, featureContext) -> {
      val previousRevision = BasicUtils
          .getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision == null) {
        return 0;
      }
      val currentTableContents = TableContentExtractor.getContent(revision);
      if (currentTableContents.length() == 0) {
        return 0;
      }
      val previousTableContents = TableContentExtractor.getContent(previousRevision);
      if (previousTableContents.length() == 0) {
        return 0;
      }
      val currentWordOccurrence = WordsExtractor.extractWords(currentTableContents);
      val previousWordOccurrence = WordsExtractor.extractWords(previousTableContents);
      val addedWordOccurrence = Multisets.difference(currentWordOccurrence, previousWordOccurrence);
      if (addedWordOccurrence.isEmpty()) {
        return 0;
      }
      return (float) addedWordOccurrence.size() / addedWordOccurrence.elementSet().size(); // calculate average occurrence of a word
    };
  }

  Feature LZWCompressionRate() {
    return (revision, ignored) -> {
      val tableContents = TableContentExtractor.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      return ZipUtil.getCompressionRatio(tableContents);
    };
  }

  Feature KLDOfCharDistribution() {
    return (revision, featureContext) -> {
      val previousRevision = BasicUtils
          .getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision == null) {
        return -1;
      }
      val currentTableContents = TableContentExtractor.getContent(revision);
      if (currentTableContents.length() == 0) {
        return -1;
      }
      val previousTableContents = TableContentExtractor.getContent(previousRevision);
      if (previousTableContents.length() == 0) {
        return -1;
      }
      return KLDUtil.calculateKLDOfAddedChars(previousTableContents, currentTableContents);
    };
  }

}

package features.content;

import com.google.common.collect.Multisets;
import features.Feature;
import lombok.val;
import util.BasicUtils;
import features.content.util.TableContentExtractor;
import util.WordsExtractor;

class TextFeatureFactory {

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

  Feature averageRelativeFrequencyOfNewAddedWords() { // FIXME to test with created corpus
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
      return (float) addedWordOccurrence.size() / addedWordOccurrence.elementSet()
          .size(); // calculate average occurrence of a word
    };
  }

}

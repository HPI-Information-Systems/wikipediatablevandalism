package features.content;

import com.google.common.collect.Multisets;
import com.google.common.math.Stats;
import features.Feature;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import util.BasicUtils;
import features.content.util.TableContentExtractor;
import util.WordsExtractor;

class TextFeatureFactory {

  Feature ratioOfNumericalCharsToAllChars() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
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
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
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
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
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
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
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
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return 0;
      }
      char charBefore = ' ';
      int longestConsecutiveSingleCharCount = 0;
      int currentConsecutiveSingleCharCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (charBefore != c || Character.isWhitespace(c)) {
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
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
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

  Feature averageRelativeFrequencyOfNewAddedWords() { // FIXME to test with deletion corpus
    return parameters -> {
      val currentTableContents = TableContentExtractor.getContent(parameters);
      if (currentTableContents.length() == 0) {
        return 0;
      }
      val previousTableContents = TableContentExtractor.getPreviousContent(parameters);
      if (previousTableContents.length() == 0) {
        return 0;
      }
      val currentWordOccurrence = WordsExtractor.extractWords(currentTableContents);
      val previousWordOccurrence = WordsExtractor.extractWords(previousTableContents);
      val addedWordOccurrence = Multisets.difference(currentWordOccurrence, previousWordOccurrence);
      if (addedWordOccurrence.isEmpty()) {
        return 0;
      }

      List<Double> addedWordFrequency = new ArrayList<>();
      for (val addedWord : addedWordOccurrence.elementSet()) {
        addedWordFrequency
            .add((double) previousWordOccurrence.count(addedWord) / previousWordOccurrence.size());
      }

      return Stats.meanOf(addedWordFrequency);
    };
  }
}

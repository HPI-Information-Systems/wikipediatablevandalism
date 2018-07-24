package features.content;

import com.google.common.collect.Multisets;
import com.google.common.math.Stats;
import features.Feature;
import features.content.util.TableContentExtractor;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import util.WordsExtractor;

class TextFeatureFactory {

  Feature ratioOfNumericalCharsToAllChars() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return -1;
      }
      double numericalCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isDigit(c)) {
          ++numericalCount;
        }
      }
      return numericalCount / tableContents.length();
    };
  }

  Feature ratioOfAlphanumericCharsToAllChars() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return -1;
      }
      double alphanumericalCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isAlphabetic(c) || Character.isDigit(c)) {
          ++alphanumericalCount;
        }
      }
      return alphanumericalCount / tableContents.length();
    };
  }

  Feature ratioOfUppercaseCharsToAllChars() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return -1;
      }
      double uppercaseCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isUpperCase(c)) {
          ++uppercaseCount;
        }
      }
      return uppercaseCount / tableContents.length();
    };
  }

  Feature ratioOfUppercaseCharsToLowercaseChars() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return -1;
      }
      double uppercaseCount = 1;
      double lowercaseCount = 1;
      for (val c : tableContents.toCharArray()) {
        if (Character.isUpperCase(c)) {
          ++uppercaseCount;
        } else if (Character.isLowerCase(c)) {
          ++lowercaseCount;
        }
      }

      return uppercaseCount / lowercaseCount;
    };
  }

  Feature lengthOfLongestConsecutiveSequenceOfSingleChar() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
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

  Feature averageRelativeFrequencyOfNewAddedWords() {
    return parameters -> {
      val currentWordOccurrence = WordsExtractor.extractWords(TableContentExtractor.getContent(parameters));
      val previousWordOccurrence = WordsExtractor.extractWords(TableContentExtractor.getPreviousContent(parameters));
      val addedWordOccurrence = Multisets.difference(currentWordOccurrence, previousWordOccurrence);
      if (addedWordOccurrence.isEmpty() || previousWordOccurrence.isEmpty()) {
        return -1;
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

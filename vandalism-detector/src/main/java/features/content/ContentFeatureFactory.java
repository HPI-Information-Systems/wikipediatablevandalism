package features.content;

import features.Feature;
import features.content.TableGeometry.Measure;
import lombok.RequiredArgsConstructor;
import lombok.val;
import matching.row.RowMatchService;

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

  // NO MATCHING FEATURES -> TODO own class?
  Feature ratioOfNumericalCharsToAllChars() {
    return (revision, ignored) -> {
      val tableContents = Utils.getContent(revision);
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
      val tableContents = Utils.getContent(revision);
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
      val tableContents = Utils.getContent(revision);
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
      val tableContents = Utils.getContent(revision);
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
      return uppercaseCount / lowercaseCount;
    };
  }

  Feature lengthOfLongestConsecutiveSequenceOfSingleChar() {
    return (revision, ignored) -> {
      val tableContents = Utils.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      char charBefore = ' ';
      int longestConsecutiveSingleCharCount = 0;
      int actualConsecutiveSingleCharCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isWhitespace(c)) {
          continue;
        }
        if (charBefore != c) {
          if (actualConsecutiveSingleCharCount > longestConsecutiveSingleCharCount) {
            longestConsecutiveSingleCharCount = actualConsecutiveSingleCharCount;
          }
          actualConsecutiveSingleCharCount = 1;
          charBefore = c;
        } else {
          ++actualConsecutiveSingleCharCount;
        }
      }
      if (actualConsecutiveSingleCharCount > longestConsecutiveSingleCharCount) { // last consecutiveSingleCharSequence could be longest
        longestConsecutiveSingleCharCount = actualConsecutiveSingleCharCount;
      }
      return longestConsecutiveSingleCharCount;
    };
  }

  Feature lengthOfLongestToken() {
    return (revision, ignored) -> {
      val tableContents = Utils.getContent(revision);
      if (tableContents.length() == 0) {
        return 0;
      }
      int longestTokenCount = 0;
      int actualTokenCount = 0;
      for (val c : tableContents.toCharArray()) {
        if (Character.isWhitespace(c)) {
          if (actualTokenCount > longestTokenCount) {
            longestTokenCount = actualTokenCount;
          }
          actualTokenCount = 0;
        } else {
          ++actualTokenCount;
        }
      }
      if (actualTokenCount > longestTokenCount) { // last token could be longest
        longestTokenCount = actualTokenCount;
      }
      return longestTokenCount;
    };
  }

  Feature previousLength() {
    return (ignored, featureContext) -> {
      val previousRevision = features.context.Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision == null) {
        return 0;
      }
      return Utils.getContent(previousRevision).length();
    };
  }

  Feature LZWCompressionRate() {
    return (revision, ignored) -> {
      return null;
    };
  }

}

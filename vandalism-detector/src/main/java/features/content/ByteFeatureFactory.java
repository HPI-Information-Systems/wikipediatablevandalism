package features.content;

import features.Feature;
import lombok.val;
import util.BasicUtils;
import features.content.util.byteP.KLDUtil;
import features.content.util.TableContentExtractor;
import features.content.util.byteP.ZipUtil;

class ByteFeatureFactory {

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

  Feature sizeChange() {
    return (revision, featureContext) -> {
      val previousRevision = BasicUtils.getPreviousRevision(featureContext.getPreviousRevisions());
      int previousRevisionParsedLength = 0;
      if (previousRevision != null) {
        previousRevisionParsedLength = BasicUtils.parsedLength(previousRevision.getParsed());
      }
      return BasicUtils.parsedLength(revision.getParsed()) - previousRevisionParsedLength;
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
      return KLDUtil.calculateKLDOfAddedChars(previousTableContents, currentTableContents);
    };
  }

  Feature commentLength() {
    return (revision, ignored) -> {
      val comment = revision.getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }

}

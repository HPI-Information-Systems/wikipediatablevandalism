package features.content;

import features.Feature;
import lombok.val;
import util.BasicUtils;
import features.content.util.byteP.KLDUtil;
import features.content.util.TableContentExtractor;
import features.content.util.byteP.ZipUtil;

class ByteFeatureFactory {

  Feature previousLength() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision(); // TODO check needed?
      if (previousRevision == null) {
        return 0;
      }
      return TableContentExtractor.getPreviousContent(parameters).length();
    };
  }

  Feature sizeChange() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      int previousRevisionParsedLength = 0;
      if (previousRevision != null) {
        previousRevisionParsedLength = BasicUtils.parsedLength(previousRevision.getParsed());
      }
      return BasicUtils.parsedLength(parameters.getRevision().getParsed()) - previousRevisionParsedLength;
    };
  }

  Feature LZWCompressionRate() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return 0;
      }
      return ZipUtil.getCompressionRatio(tableContents);
    };
  }

  Feature KLDOfCharDistribution() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      if (previousRevision == null) { // TODO check needed?
        return 0;
      }
      val currentTableContents = TableContentExtractor.getContent(parameters);
      if (currentTableContents.length() == 0) {
        return 0;
      }
      val previousTableContents = TableContentExtractor.getPreviousContent(parameters);
      if (previousTableContents.length() == 0) {
        return 0;
      }
      return KLDUtil.calculateKLDOfAddedChars(previousTableContents, currentTableContents);
    };
  }

  Feature commentLength() {
    return parameters -> {
      val comment = parameters.getRevision().getComment();

      if (comment == null || comment.getValue() == null) {
        return 0;
      }

      return comment.getValue().length();
    };
  }

}

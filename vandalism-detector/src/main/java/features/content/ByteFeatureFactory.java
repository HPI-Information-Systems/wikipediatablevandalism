package features.content;

import features.Feature;
import features.content.util.TableContentExtractor;
import features.content.util.byteP.KLD;
import features.content.util.byteP.Zip;
import lombok.val;
import util.BasicUtils;

class ByteFeatureFactory {

  Feature previousLength() {
    return parameters -> TableContentExtractor.getPreviousContent(parameters).length();
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

  Feature sizeRatio() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      int previousRevisionParsedLength = 0;
      if (previousRevision != null) {
        previousRevisionParsedLength = BasicUtils.parsedLength(previousRevision.getParsed());
      }
      return (double) (BasicUtils.parsedLength(parameters.getRevision().getParsed()) - previousRevisionParsedLength + 1) / (previousRevisionParsedLength + 1);
    };
  }

  Feature LZWCompressionRate() {
    return parameters -> {
      val tableContents = TableContentExtractor.getContent(parameters);
      if (tableContents.length() == 0) {
        return 0;
      }
      return Zip.getCompressionRatio(tableContents);
    };
  }

  Feature KLDOfCharDistribution() {
    return parameters -> KLD.calculateKLDOfAddedChars(TableContentExtractor.getPreviousContent(parameters), TableContentExtractor.getContent(parameters));
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

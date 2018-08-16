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
      return BasicUtils.parsedLength(parameters.getRevision().getParsed())
          - previousRevisionParsedLength;
    };
  }

  Feature sizeRatio() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      int previousRevisionParsedLength = 0;
      if (previousRevision != null) {
        previousRevisionParsedLength = BasicUtils.parsedLength(previousRevision.getParsed());
      }
      return (double) (BasicUtils.parsedLength(parameters.getRevision().getParsed())
          - previousRevisionParsedLength + 1) / (previousRevisionParsedLength + 1);
    };
  }

  Feature LZWCompressionRate() {
    return new Zip();
  }

  Feature kldOfAddedCharDistribution() {
    return KLD::kldOfAddedChars;
  }

  Feature kldOfCharDistribution() {
    return KLD::kld;
  }

  Feature rawCommentLength() {
    return parameters -> parameters.getRawComment().length();
  }

  Feature userCommentLength() {
    return parameters -> parameters.getUserComment().length();
  }
}

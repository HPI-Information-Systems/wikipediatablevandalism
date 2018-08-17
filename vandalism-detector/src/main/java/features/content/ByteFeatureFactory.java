package features.content;

import features.Feature;
import features.content.util.byteP.KLD;
import features.content.util.byteP.Zip;
import java.nio.charset.StandardCharsets;
import util.RatioUtil;

class ByteFeatureFactory {

  Feature previousLength() {
    return parameters -> {
      final String content = parameters.getContent();
      return kilobytesOf(content);
    };
  }

  Feature sizeChange() {
    return parameters -> {
      final String content = parameters.getContent();
      final String previousContent = parameters.getPreviousContent();
      return kilobytesOf(content) - kilobytesOf(previousContent);
    };
  }

  Feature addSizeRatio() {
    return parameters -> {
      final long previous = kilobytesOf(parameters.getPreviousContent());
      final long current = kilobytesOf(parameters.getContent());
      return RatioUtil.added(previous, current);
    };
  }

  Feature removedSizeRatio() {
    return parameters -> {
      final long previous = kilobytesOf(parameters.getPreviousContent());
      final long current = kilobytesOf(parameters.getContent());
      return RatioUtil.removed(previous, current);
    };
  }

  private long kilobytesOf(final String string) {
    return string.getBytes(StandardCharsets.UTF_8).length / 1024;
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

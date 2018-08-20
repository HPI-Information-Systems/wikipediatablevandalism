package features.content.util.byteP;

import com.google.common.annotations.VisibleForTesting;
import features.Feature;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.val;
import model.FeatureParameters;
import org.apache.commons.lang3.StringUtils;

/**
 * Zip compression ratio: compressed(content).length / uncompressed(content).length.
 *
 * Towards 0: compressed length is very short, redundant information present Towards 1: content
 * entropy is high
 */
public class Zip implements Feature {

  @VisibleForTesting
  static final Charset CHARSET = StandardCharsets.UTF_8;

  @Override
  public double getValue(FeatureParameters parameters) {
    val tableContents = parameters.getContentWithComment();
    if (StringUtils.isBlank(tableContents)) {
      return 0;
    }
    return getCompressionRatio(tableContents);
  }

  @VisibleForTesting
  static double getCompressionRatio(final String value) {
    final byte[] compressed = compressedZip(value);
    final byte[] uncompressed = uncompressedZip(value);
    return (double) compressed.length / uncompressed.length;
  }

  @VisibleForTesting
  static byte[] compressedZip(final String value) {
    return toZip(value, Deflater.DEFAULT_COMPRESSION);
  }

  private static byte[] uncompressedZip(final String value) {
    return toZip(value, Deflater.NO_COMPRESSION);
  }

  private static byte[] toZip(final String value, final int level) {
    val out = new ByteArrayOutputStream();
    try (val zip = new ZipOutputStream(out)) {
      zip.setLevel(level);

      zip.putNextEntry(new ZipEntry(""));
      zip.write(value.getBytes(CHARSET));
      zip.closeEntry();

      return out.toByteArray();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}

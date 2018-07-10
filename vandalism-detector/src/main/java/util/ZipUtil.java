package util;

import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.val;

public class ZipUtil {

  @VisibleForTesting
  static final Charset CHARSET = StandardCharsets.UTF_8;

  public static double getCompressionRatio(final String value) {
    final byte[] compressed = compress(value);
    final byte[] uncompressed = value.getBytes(CHARSET);
    return (double) compressed.length / uncompressed.length;
  }

  @VisibleForTesting
  static byte[] compress(final String value) {
    val out = new ByteArrayOutputStream();
    try (val zip = new ZipOutputStream(out)) {
      zip.putNextEntry(new ZipEntry(""));
      zip.write(value.getBytes(CHARSET));
      zip.closeEntry();
      return out.toByteArray();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}

package features.content.util.byteP;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import lombok.val;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZipUtilTest {

  private String value;

  @BeforeEach
  void setUp() {
    value =
        "This wharf comprises a single long pier that ends in a pontoon with two docking " +
            "stations. However, only one of them is used at a time due to size constraints. " +
            "This terminal has a covered passenger waiting area with a Telstra pay phone and a " +
            "drinking water fountain. It takes its name from the adjacent Guyatt Park. " +
            "The wharf sustained moderate damage during the January 2011 Brisbane floods.[3] " +
            "It reopened after repairs on 14 February 2011.[4][5]";
  }

  @Test
  void saneCompressionRatio() {
    final double actual = ZipUtil.getCompressionRatio(value);

    assertThat(actual).isCloseTo(0.75, Offset.offset(0.01));
  }

  @Test
  void canCompress() {
    final byte[] compressed = ZipUtil.compress(value);
    final byte[] uncompressed = uncompress(compressed);
    assertThat(uncompressed).isEqualTo(value.getBytes(ZipUtil.CHARSET));
  }

  private byte[] uncompress(final byte[] value) {
    val in = new ByteArrayInputStream(value);
    try (val zip = new ZipInputStream(in)) {
      zip.getNextEntry();
      return ByteStreams.toByteArray(zip);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

}

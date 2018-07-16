package util;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import lombok.val;

public class WordListUtil {

  public static Set<String> read(final String filename) {
    val url = WordListUtil.class.getClassLoader().getResource(filename);

    if (url == null) {
      throw new IllegalArgumentException(filename + " does not exist");
    }

    try {
      final Set<String> lines = new HashSet<>(Resources.readLines(url, StandardCharsets.UTF_8));
      lines.remove("");
      return ImmutableSet.copyOf(lines);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}

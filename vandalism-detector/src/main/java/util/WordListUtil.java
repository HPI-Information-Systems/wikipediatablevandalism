package util;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import lombok.val;

public class WordListUtil {

  public static Set<String> read(final String filename) {
    try {
      val url = WordListUtil.class.getClassLoader().getResource(filename);
      if (url == null) {
        throw new IllegalArgumentException(filename + " does not exist");
      }

      final Set<String> words = new HashSet<>(Files.readAllLines(Paths.get(url.toURI())));
      words.remove("");
      return ImmutableSet.copyOf(words);
    } catch (final IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}

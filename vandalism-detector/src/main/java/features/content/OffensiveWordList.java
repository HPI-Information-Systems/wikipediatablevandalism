package features.content;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import lombok.val;

class OffensiveWordList {

  // Downloaded from http://www.cs.cmu.edu/~biglou/resources/
  private static final String FILENAME = "bad-words.txt";

  private static Set<String> words = read();

  private static Set<String> read() {
    try {
      val url = OffensiveWordList.class.getClassLoader().getResource(FILENAME);
      if (url == null) {
        throw new IllegalArgumentException(FILENAME + " does not exist");
      }

      final Set<String> words = new HashSet<>(Files.readAllLines(Paths.get(url.toURI())));
      words.remove("");
      return ImmutableSet.copyOf(words);
    } catch (final IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  static Set<String> getWords() {
    return words;
  }
}

package features.context;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import lombok.val;

class BotList {

  private static final String FILENAME = "botlist.txt";

  static Set<String> read() {
    try {
      val url = BotList.class.getClassLoader().getResource(FILENAME);
      if (url == null) {
        throw new IllegalArgumentException(FILENAME + " does not exist");
      }

      val bots = Files.readAllLines(Paths.get(url.toURI()));
      return ImmutableSet.copyOf(bots);
    } catch (final IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}

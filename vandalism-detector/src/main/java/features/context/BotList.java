package features.context;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Set;
import lombok.val;
import util.WordListUtil;

class BotList {

  // extracted from https://en.wikipedia.org/w/index.php?title=Special:ListUsers/bot&offset=&limit=500&group=bot
  private static final String FILENAME = "botlist.txt";

  static Set<String> read() {
      return WordListUtil.read(FILENAME);
    }
  }

package features.context.util;

import java.util.Set;
import util.WordListUtil;

public class BotList {

  // extracted from https://en.wikipedia.org/w/index.php?title=Special:ListUsers/bot&offset=&limit=500&group=bot
  private static final String FILENAME = "botlist.txt";

  public static Set<String> read() {
      return WordListUtil.read(FILENAME);
    }
  }

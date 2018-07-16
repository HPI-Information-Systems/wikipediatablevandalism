package features.language;

import java.util.Set;
import util.WordListUtil;

class DictionaryWordList {

  private static final String FILENAME = "english-words.txt";

  private static Set<String> WORDS = WordListUtil.read(FILENAME);

  static Set<String> getWords() {
    return WORDS;
  }
}

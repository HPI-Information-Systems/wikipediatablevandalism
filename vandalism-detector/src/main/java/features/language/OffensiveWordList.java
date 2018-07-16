package features.language;

import java.util.Set;
import util.WordListUtil;

class OffensiveWordList {

  // Downloaded from http://www.cs.cmu.edu/~biglou/resources/
  private static final String FILENAME = "bad-words.txt";

  private static Set<String> words = WordListUtil.read(FILENAME);

  static Set<String> getWords() {
    return words;
  }
}

package features.language.wordlists;

import java.util.Set;
import util.WordListUtil;

public class OffensiveWordList {

  // Downloaded from http://www.cs.cmu.edu/~biglou/resources/
  private static final String FILENAME = "bad-words.txt";

  private static Set<String> words = WordListUtil.read(FILENAME);

  public static Set<String> getWords() {
    return words;
  }
}

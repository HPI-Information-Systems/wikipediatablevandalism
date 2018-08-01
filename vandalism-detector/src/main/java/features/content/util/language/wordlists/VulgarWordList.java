package features.content.util.language.wordlists;

import java.util.Set;
import util.WordListUtil;

/**
 * List of offensive, vulgar words. Downloaded from http://www.cs.cmu.edu/~biglou/resources/
 * Removed nationalities and religious terms.
 */
public class VulgarWordList {

  private static final String FILENAME = "vulgar-words.txt";

  private static Set<String> words = WordListUtil.readStemms(FILENAME);

  public static Set<String> getWords() {
    return words;
  }
}

package features.language.wordlists;

import java.util.Set;
import util.WordListUtil;

/**
 * List of wikipedia syntax elements
 * Download
 */
public class WikiSyntaxWordList {

  private static final String FILENAME = "wiki-syntax-words.txt";

  private static Set<String> WORDS = WordListUtil.read(FILENAME);

  public static Set<String> getWords() {
    return WORDS;
  }
}

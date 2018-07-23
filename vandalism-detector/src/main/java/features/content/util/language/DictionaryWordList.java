package features.content.util.language;

import java.util.Set;
import util.WordListUtil;

/**
 * List of English dictionary words.
 *
 * @see <a href="https://github.com/dwyl/english-words">GH: dwyl/english-words</a>
 */
class DictionaryWordList {

  private static final String FILENAME = "english-words.txt";

  private static Set<String> WORDS = WordListUtil.read(FILENAME);

  static Set<String> getWords() {
    return WORDS;
  }

}
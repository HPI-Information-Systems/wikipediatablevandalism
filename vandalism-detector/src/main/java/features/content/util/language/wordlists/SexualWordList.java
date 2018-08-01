package features.content.util.language.wordlists;

import java.util.Set;
import util.WordListUtil;

/**
 * List of non-vulgar, sexual words. Downloaded from https://sexetc.org/sex-ed/sex-terms/?pageNum=70
 */
public class SexualWordList {

  private static final String FILENAME = "sexual-words.txt";

  private static Set<String> words = WordListUtil.readStemms(FILENAME);

  public static Set<String> getWords() {
    return words;
  }
}

package features.language;

import java.util.Set;
import util.WordListUtil;

public class PronounWordList {

  // Personal pronouns (1. & 2. person)
  // Downloaded from https://en.oxforddictionaries.com/grammar/pronouns
  private static final String FILENAME = "personal-pronouns.txt";

  private static Set<String> words = WordListUtil.read(FILENAME);

  static Set<String> getWords() {
    return words;
  }
}

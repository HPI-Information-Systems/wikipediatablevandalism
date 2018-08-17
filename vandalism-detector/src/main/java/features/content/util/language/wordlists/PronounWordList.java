package features.content.util.language.wordlists;

import java.util.Set;
import util.WordListUtil;

public class PronounWordList {

  // Personal pronouns (1. & 2. person)
  // Downloaded from https://en.oxforddictionaries.com/grammar/pronouns
  private static Set<String> ALL = WordListUtil.read("personal-pronouns.txt");
  private static Set<String> SINGULAR = WordListUtil.read("personal-pronouns-singular.txt");

  public static Set<String> getAll() {
    return ALL;
  }

  public static Set<String> getSingular() {
    return SINGULAR;
  }
}

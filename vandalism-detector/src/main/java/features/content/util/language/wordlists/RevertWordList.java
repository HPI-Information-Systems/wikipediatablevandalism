package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import util.StemmerUtils;

public class RevertWordList {

  private static Set<String> words = StemmerUtils.stem(ImmutableSet.of(
      "revert",
      "reverted",
      "reverting",
      "rollback",
      "rv",
      "rvv",
      "undo",
      "undid",
      "rvrt"
  ));

  public static Set<String> getWords() {
    return words;
  }
}

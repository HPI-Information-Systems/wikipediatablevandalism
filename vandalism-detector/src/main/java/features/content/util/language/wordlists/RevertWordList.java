package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class RevertWordList {

  private static Set<String> words = ImmutableSet.of(
      "revert",
      "reverted",
      "reverting",
      "rollback",
      "rv",
      "rvv",
      "undo",
      "undid"
  );

  public static Set<String> getWords() {
    return words;
  }
}

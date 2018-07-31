package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class ReplaceWordList {

  private static Set<String> words = ImmutableSet.of(
      "replace",
      "replaced"
  );

  public static Set<String> getWords() {
    return words;
  }
}

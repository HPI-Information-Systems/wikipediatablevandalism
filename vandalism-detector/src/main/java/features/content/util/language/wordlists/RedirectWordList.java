package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class RedirectWordList {

  private static Set<String> words = ImmutableSet.of(
      "Redirect",
      "Direct"
  );

  public static Set<String> getWords() {
    return words;
  }
}

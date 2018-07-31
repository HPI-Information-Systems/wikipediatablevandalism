package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class RedirectWordList {

  private static Set<String> words = ImmutableSet.of(
      "redirect",
      "redirected",
      "direct",
      "directed",
      "redir",
      "rdr"
  );

  public static Set<String> getWords() {
    return words;
  }
}

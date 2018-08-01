package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import util.StemmerUtils;

public class RedirectWordList {

  private static Set<String> words = StemmerUtils.stem(ImmutableSet.of(
      "redirect",
      "redirected",
      "direct",
      "directed",
      "redir",
      "rdr"
  ));

  public static Set<String> getWords() {
    return words;
  }
}

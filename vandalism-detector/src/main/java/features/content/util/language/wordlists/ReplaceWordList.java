package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import util.StemmerUtils;

public class ReplaceWordList {

  private static Set<String> words = StemmerUtils.stem(ImmutableSet.of(
      "replace",
      "replaced"
  ));

  public static Set<String> getWords() {
    return words;
  }
}

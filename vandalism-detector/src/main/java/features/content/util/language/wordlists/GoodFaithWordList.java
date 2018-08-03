package features.content.util.language.wordlists;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import util.StemmerUtils;

public class GoodFaithWordList {

  private static Set<String> words = StemmerUtils.stem(ImmutableSet.of(
      "good faith",
      "goodfaith",
      "gf",
      "assume good faith",
      "assumming good faith",
      "agf",
      "agfc",
      "gfc",
      "asg",
      "asgf",
      "faith"
  ));

  public static Set<String> getWords() {
    return words;
  }
}

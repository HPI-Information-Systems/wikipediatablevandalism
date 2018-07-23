package features.content.util.language.regex;

import java.util.Set;
import java.util.regex.Pattern;

public final class RegexUtil {

  public static int countMatches(Set<Pattern> patterns, Set<String> words) {
    int matches = 0;

    for (String word : words) {
      for (Pattern pattern : patterns) {
        if (pattern.matcher(word).matches()) {
          matches += 1;
          // Match a word to only one regex
          break;
        }
      }
    }
    return matches;
  }

  private RegexUtil() {
  }

}

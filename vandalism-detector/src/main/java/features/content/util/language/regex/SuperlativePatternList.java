package features.content.util.language.regex;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SuperlativePatternList {

  // Basic patterns to find comparatives and superlatives
  // From: https://dictionary.cambridge.org/grammar/british-grammar/comparatives-and-superlatives/comparison-adjectives-bigger-biggest-more-interesting
  private static Set<Pattern> PATTERNS = ImmutableSet.of(
      Pattern.compile("\\b(\\w*est)\\b"),
      Pattern.compile("\\b(\\w*er)\\b")
  );

  public static Set<Pattern> getPatterns() {
    return PATTERNS;
  }
}

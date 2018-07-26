package features.content.util.language.regex;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Patterns to detect wikitext syntax elements Reference: https://en.wikipedia.org/wiki/Help:Wikitext
 */
public class WikiSyntaxPatternList {

  static Pattern BOLD_ITALIC = Pattern.compile("(\\'\\'.*?\\'\\')");
  static Pattern LINKS = Pattern.compile("(\\[\\[.*?\\]\\])");
  static Pattern MATH_FORMULA = Pattern.compile("(<math(.*?)>.*?<\\/math>)");
  static Pattern SUPERSCRIPT = Pattern.compile("(<sup>.*?<\\/sup>)");
  static Pattern SUBSCRIPT = Pattern.compile("(<sub>.*?<\\/sub>)");
  static Pattern POEM = Pattern.compile("(<poem>.*?<\\/poem>)");
  static Pattern CODE = Pattern.compile("(<code>.*?<\\/code>)");
  static Pattern BLOCKQUOTE = Pattern.compile("(<blockquote>.*?<\\/blockquote>)");
  static Pattern DIV = Pattern.compile("(<div(.*?)>.*?<\\/div>)");
  static Pattern STACK_TEXT = Pattern.compile("(\\{\\{stack|(.*?)\\}\\})");
  static Pattern TABLE_OF_CONTENTS = Pattern.compile("(__TOC__)");
  static Pattern COMMENT = Pattern.compile("(<!--.*?-->)");
  static Pattern HEADING = Pattern.compile("(==.*?==)");
  static Pattern UNORDERED_LIST = Pattern.compile("\\*[^,#]+");
  static Pattern ORDERED_LIST = Pattern.compile("\\#[^,#]+");
  static Pattern DEFINITION_LIST = Pattern.compile("[;:][^,;:]+");
  static Pattern NON_BREAKING_SPACE = Pattern.compile("(&nbsp;)");

  // Basic patterns to find wikisyntax text formatting elements
  // From: https://dictionary.cambridge.org/grammar/british-grammar/comparatives-and-superlatives/comparison-adjectives-bigger-biggest-more-interesting
  private static Set<Pattern> PATTERNS = ImmutableSet.of(
      BOLD_ITALIC,
      LINKS,
      MATH_FORMULA,
      SUPERSCRIPT,
      SUBSCRIPT,
      POEM,
      CODE,
      BLOCKQUOTE,
      DIV,
      STACK_TEXT,
      TABLE_OF_CONTENTS,
      COMMENT,
      HEADING,
      UNORDERED_LIST,
      ORDERED_LIST,
      DEFINITION_LIST,
      NON_BREAKING_SPACE);

  public static Set<Pattern> getPatterns() {
    return PATTERNS;
  }
}

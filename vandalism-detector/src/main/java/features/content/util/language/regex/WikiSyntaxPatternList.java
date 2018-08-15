package features.content.util.language.regex;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Patterns to detect wikitext syntax elements Reference: https://en.wikipedia.org/wiki/Help:Wikitext
 */
public class WikiSyntaxPatternList {

  static Pattern ITALIC = Pattern.compile("(\'\'.*?\'\')", Pattern.DOTALL);
  static Pattern BOLD = Pattern.compile("(\'\'\'.*?\'\'\')", Pattern.DOTALL);
  static Pattern BOLD_ITALIC = Pattern.compile("(\'\'\'\'\'.*?\'\'\'\'\')", Pattern.DOTALL);
  static Pattern LINKS_INTERN = Pattern.compile("(\\[\\[.*?]])", Pattern.DOTALL);
  static Pattern LINKS_EXTERN = Pattern.compile("(https?://)");
  static Pattern REFS = Pattern.compile("((<ref(.*?)>.*?</ref>)|(<ref(.*?)/>))", Pattern.DOTALL);
  static Pattern REF_LIST = Pattern.compile("(<references />)");
  static Pattern CURLY_BRACES = Pattern.compile("(\\{\\{.*?}}|\\{\\{\\{.*?}}})", Pattern.DOTALL);
  static Pattern MATH_FORMULA = Pattern.compile("(<math(.*?)>.*?</math>)", Pattern.DOTALL);
  static Pattern SPAN = Pattern.compile("(<span(.*?)>.*?</span>)", Pattern.DOTALL);
  static Pattern SYNTAXHIGHLIGHT = Pattern.compile("(<syntaxhighlight(.*?)>.*?</syntaxhighlight>)", Pattern.DOTALL);
  static Pattern SMALL = Pattern.compile("(<small(.*?)>.*?</small>)", Pattern.DOTALL);
  static Pattern BIG = Pattern.compile("(<big(.*?)>.*?</big>)", Pattern.DOTALL);
  static Pattern DIV = Pattern.compile("(<div(.*?)>.*?</div>)", Pattern.DOTALL);
  static Pattern POEM = Pattern.compile("(<poem(.*?)>.*?</poem>)", Pattern.DOTALL);
  static Pattern PRE = Pattern.compile("(<pre(.*?)>.*?</pre>)", Pattern.DOTALL);
  static Pattern SUPERSCRIPT = Pattern.compile("(<sup>.*?</sup>)", Pattern.DOTALL);
  static Pattern S = Pattern.compile("(<s>.*?</s>)", Pattern.DOTALL);
  static Pattern U = Pattern.compile("(<u>.*?</u>)", Pattern.DOTALL);
  static Pattern DEL = Pattern.compile("(<del>.*?</del>)", Pattern.DOTALL);
  static Pattern INS = Pattern.compile("(<ins>.*?</ins>)", Pattern.DOTALL);
  static Pattern SUBSCRIPT = Pattern.compile("(<sub>.*?</sub>)", Pattern.DOTALL);
  static Pattern CODE = Pattern.compile("(<code>.*?</code>)", Pattern.DOTALL);
  static Pattern BLOCKQUOTE = Pattern.compile("(<blockquote>.*?</blockquote>)", Pattern.DOTALL);
  static Pattern TABLE_OF_CONTENTS = Pattern.compile("(__TOC__|__FORCETOC__|__NOTOC__)");
  static Pattern COMMENT = Pattern.compile("(<!--.*?-->)", Pattern.DOTALL);
  static Pattern NOWIKI = Pattern.compile("((<nowiki>.*?</nowiki>)|<nowiki />)", Pattern.DOTALL);
  static Pattern NOINCLUDE = Pattern.compile("(<noinclude>)");
  static Pattern INCLUDEONLY = Pattern.compile("(includeonly)>");
  static Pattern ONLYINCLUDE = Pattern.compile("(<onlyinclude>)");
  static Pattern DL = Pattern.compile("(<dl>.*?</dl>)", Pattern.DOTALL);
  static Pattern DT = Pattern.compile("(<dt>.*?</dt>)", Pattern.DOTALL);
  static Pattern DD = Pattern.compile("(<dd>.*?</dd>)", Pattern.DOTALL);
  static Pattern SCORE = Pattern.compile("(<score>.*?</score>)", Pattern.DOTALL);
  static Pattern HEADING = Pattern.compile("((=.*?=)|(==.*?==)|(===.*?===)|(====.*?====)|(=====.*?=====)|(======.*?======))", Pattern.DOTALL);
  static Pattern UNORDERED_LIST = Pattern.compile("(\\*[^,#]+)");
  static Pattern ORDERED_LIST = Pattern.compile("(#[^,#]+)");
  static Pattern DEFINITION_LIST = Pattern.compile("([;:][^,;:]+)");
  static Pattern NON_BREAKING_SPACE = Pattern.compile("(&nbsp;)");
  static Pattern HORIZONTAL_RULE_WIKI = Pattern.compile("(----)");
  static Pattern HORIZONTAL_RULE_HTML = Pattern.compile("(<hr />)");
  static Pattern BR = Pattern.compile("(<br>|<br />)");

  // Basic patterns to find wikisyntax text formatting elements
  // From: https://dictionary.cambridge.org/grammar/british-grammar/comparatives-and-superlatives/comparison-adjectives-bigger-biggest-more-interesting
  private static Set<Pattern> PATTERNS = ImmutableSet.of(
      BOLD,
      ITALIC,
      BOLD_ITALIC,
      LINKS_INTERN,
      LINKS_EXTERN,
      REFS,
      REF_LIST,
      CURLY_BRACES,
      MATH_FORMULA,
      SPAN,
      SUPERSCRIPT,
      S,
      U,
      DEL,
      INS,
      PRE,
      SUBSCRIPT,
      POEM,
      CODE,
      BLOCKQUOTE,
      DIV,
      TABLE_OF_CONTENTS,
      COMMENT,
      NOWIKI,
      NOINCLUDE,
      INCLUDEONLY,
      ONLYINCLUDE,
      HEADING,
      UNORDERED_LIST,
      ORDERED_LIST,
      DEFINITION_LIST,
      NON_BREAKING_SPACE,
      HORIZONTAL_RULE_WIKI,
      HORIZONTAL_RULE_HTML,
      DL,
      DT,
      DD,
      BR,
      SYNTAXHIGHLIGHT,
      SMALL,
      BIG,
      SCORE
  );

  public static Set<Pattern> getPatterns() {
    return PATTERNS;
  }
}

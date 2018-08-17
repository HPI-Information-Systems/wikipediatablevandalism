package features.content.util;

import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.RatioUtil;

/**
 * Model changes in references (article sources).
 *
 * <p>The identity of the reference is deliberately excluded; replacing a source cannot be judged
 * (except if we had a measure for source credibility).</p>
 */
public class RefChange {

  static Pattern REF_HTML = Pattern.compile("<ref(.*?)>.*?</ref>", Pattern.DOTALL);
  static Pattern REF_WIKI_SYNTAX = Pattern.compile("\\[\\[ref:.*?]]", Pattern.DOTALL);

  /**
   * X % of references have been removed
   */
  public static double getRemovedRatio(FeatureParameters parameters) {
    double previous = getRefCount(TableContentExtractor.getPreviousContent(parameters));
    double current = getRefCount(TableContentExtractor.getContent(parameters));
    return RatioUtil.removed(previous, current);
  }

  /**
   * There has been an x-fold increase in references.
   */
  public static double getAddedRatio(FeatureParameters parameters) {
    double previous = getRefCount(TableContentExtractor.getPreviousContent(parameters));
    double current = getRefCount(TableContentExtractor.getContent(parameters));
    return RatioUtil.added(previous, current);
  }

  /**
   * Article contains currently X references
   */
  public static double getCount(FeatureParameters parameters) {
    return getRefCount(TableContentExtractor.getContent(parameters));
  }

  private static double getRefCount(String content) {
    double refCount = 0;
    val htmlMatcher = REF_HTML.matcher(content);
    val wikiSyntaxMatcher = REF_WIKI_SYNTAX.matcher(content);

    while (htmlMatcher.find() || wikiSyntaxMatcher.find()) {
      ++refCount;
    }

    return refCount;
  }
}

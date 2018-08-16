package features.content.util;

import com.google.common.annotations.VisibleForTesting;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;

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
    double currentRefCount = getRefCount(TableContentExtractor.getContent(parameters));
    double previousRefCount = getRefCount(TableContentExtractor.getPreviousContent(parameters));
    return getRemovedRatio(previousRefCount, currentRefCount);
  }

  @VisibleForTesting
  static double getRemovedRatio(final double previousRefCount, final double currentRefCount) {
    if (previousRefCount == 0) {
      return 0;
    }

    double removedRefCount = previousRefCount - currentRefCount;
    return Math.max(0, removedRefCount) / previousRefCount;
  }

  /**
   * There has been an x-fold increase in references.
   */
  public static double getAddedRatio(FeatureParameters parameters) {
    double currentRefCount = getRefCount(TableContentExtractor.getContent(parameters));
    double previousRefCount = getRefCount(TableContentExtractor.getPreviousContent(parameters));
    return getAddedRatio(previousRefCount, currentRefCount);
  }

  @VisibleForTesting
  static double getAddedRatio(final double previousRefCount, final double currentRefCount) {
    if (currentRefCount == 0) {
      return 0;
    }

    if (previousRefCount == 0) {
      return 1;
    }

    double addedRefCount = currentRefCount - previousRefCount;
    if (addedRefCount > 0) {
      return 1 + addedRefCount / previousRefCount;
    }
    return 0;
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

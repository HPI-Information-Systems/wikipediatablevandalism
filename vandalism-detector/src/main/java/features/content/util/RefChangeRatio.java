package features.content.util;

import features.Feature;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;

public class RefChangeRatio implements Feature {

  static Pattern REF_HTML = Pattern.compile("<ref(.*?)>.*?</ref>", Pattern.DOTALL);
  static Pattern REF_WIKI_SYNTAX = Pattern.compile("\\[\\[ref:.*?\\]\\]", Pattern.DOTALL);

  @Override
  public double getValue(FeatureParameters parameters) {
    double currentRefCount = getRefCount(TableContentExtractor.getContent(parameters));
    double previousRefCount = getRefCount(TableContentExtractor.getPreviousContent(parameters));
    if (previousRefCount == 0) {
      if (currentRefCount == 0) {
        return 0;
      }
      return 1;
    }
    return (currentRefCount - previousRefCount) / previousRefCount;
  }

  private double getRefCount(String content) {
    double refCount = 0;
    val htmlMatcher = REF_HTML.matcher(content);
    val wikiSyntaxMatcher = REF_WIKI_SYNTAX.matcher(content);

    while (htmlMatcher.find() || wikiSyntaxMatcher.find()) {
      ++refCount;
    }

    return refCount;
  }

}

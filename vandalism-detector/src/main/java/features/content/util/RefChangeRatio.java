package features.content.util;

import features.Feature;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;

public class RefChangeRatio implements Feature {

  static Pattern REF = Pattern.compile("<ref(\\s+\\p{Graph}+\\s*=\\s*\"(\\s*\\p{Graph})+\\s*\")*\\s*>.*?</\\s*r\\s*e\\s*f\\s*>", Pattern.DOTALL);

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
    val currentMatcherRef = REF.matcher(content);
    while (currentMatcherRef.find()) {
      ++refCount;
    }
    return refCount;
  }

}

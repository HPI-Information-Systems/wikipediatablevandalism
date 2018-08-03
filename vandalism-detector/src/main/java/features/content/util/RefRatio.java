package features.content.util;

import features.Feature;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;

public class RefRatio implements Feature {

  static Pattern REF = Pattern.compile("<ref(\\s+\\p{Graph}+\\s*=\\s*\"\\s*\\p{Graph}+\\s*\")*\\s*>.*?</\\s*r\\s*e\\s*f\\s*>");

  @Override
  public double getValue(FeatureParameters parameters) {
    double size = BasicUtils.parsedLength(parameters.getRevision().getParsed());
    int refCount = 0;
    val matcherOpenWiki = REF.matcher(TableContentExtractor.getContent(parameters));
    while (matcherOpenWiki.find()) {
      ++refCount;
    }
    if (size == 0) {
      return -1;
    }

    return refCount / size;
  }
}

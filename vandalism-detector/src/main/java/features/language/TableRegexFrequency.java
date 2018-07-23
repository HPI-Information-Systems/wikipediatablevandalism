package features.language;

import static util.RegexUtil.countMatches;

import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.DiffUtil;

/**
 * Number of matching words to a list of regex patterns relative to the size of the edit in tables.
 */
public class TableRegexFrequency implements Feature {

  private final Set<Pattern> regularExpressions;

  TableRegexFrequency(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    val diffWords = DiffUtil.diffWords(parameters);
    val matches = countMatches(this.regularExpressions, diffWords.elementSet());
    return diffWords.size() > 0 ? ((float) matches / diffWords.size()) : 0;
  }
}

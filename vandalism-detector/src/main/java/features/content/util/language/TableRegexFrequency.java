package features.content.util.language;

import static features.content.util.language.regex.RegexUtil.countMatches;

import features.Feature;
import features.content.util.TableContentExtractor;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.DiffUtil;
import util.WordsExtractor;

/**
 * Number of matching words to a list of regex patterns relative to the size of the edit in tables.
 */
public class TableRegexFrequency implements Feature {

  private final Set<Pattern> regularExpressions;

  public TableRegexFrequency(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val diffWords = DiffUtil.diffWords(parameters);
    val matches = countMatches(this.regularExpressions, diffWords.elementSet());
    return diffWords.size() > 0 ? ((double) matches / diffWords.size()) : 0;
  }
}

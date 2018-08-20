package features.content.util.language;

import static features.content.util.language.regex.RegexUtil.countMatches;

import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import util.WordsExtractor;

/**
 * Percentage increase of matching words to a list of regex patterns compared to previous revision.
 */
public class TableRegexImpact implements Feature {

  private final Set<Pattern> regularExpressions;

  public TableRegexImpact(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val words = WordsExtractor.extractWords(BasicUtils.getCurrentChangedTables(parameters));
    val previousWords = WordsExtractor
        .extractWords(BasicUtils.getPreviousChangedTables(parameters));

    val previousMatches = countMatches(this.regularExpressions, previousWords.elementSet());
    val matches = countMatches(this.regularExpressions, words.elementSet());
    val previousMatchCount = previousMatches > 0 ? previousMatches : 1;
    return ((double) (matches - previousMatches) / previousMatchCount);
  }
}

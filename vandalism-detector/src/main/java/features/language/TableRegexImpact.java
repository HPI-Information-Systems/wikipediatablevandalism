package features.language;

import static util.RegexUtil.countMatches;

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
class TableRegexImpact implements Feature {

  private final Set<Pattern> regularExpressions;

  TableRegexImpact(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    val words = WordsExtractor.extractWords(BasicUtils.getCurrentTables(parameters));
    val previousWords = WordsExtractor.extractWords(BasicUtils.getPreviousTables(parameters));

    val previousMatches = countMatches(this.regularExpressions, previousWords.elementSet());
    val matches = countMatches(this.regularExpressions, words.elementSet());
    val previousMatchCount = previousMatches > 0 ? previousMatches : 1;
    return ((float) matches / previousMatchCount);
  }
}

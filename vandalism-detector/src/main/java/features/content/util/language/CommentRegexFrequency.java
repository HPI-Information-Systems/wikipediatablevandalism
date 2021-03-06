package features.content.util.language;

import static features.content.util.language.regex.RegexUtil.countMatches;

import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.WordsExtractor;

/**
 * Number of matching words to a list of regex patterns relative to the size of comment size.
 */
public class CommentRegexFrequency implements Feature {

  private final Set<Pattern> regularExpressions;

  public CommentRegexFrequency(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val comment = parameters.getUserComment();
    val words = WordsExtractor.extractWords(comment);
    val matches = countMatches(this.regularExpressions, words.elementSet());
    return words.size() > 0 ? ((double) matches / words.size()) : 0;
  }
}

package features.language;

import static util.RegexUtil.countMatches;

import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import org.apache.commons.lang3.StringUtils;
import util.WordsExtractor;

/**
 * Number of matching words to a list of regex patterns relative to the size of comment size.
 */
public class CommentRegexFrequency implements Feature {

  private final Set<Pattern> regularExpressions;

  CommentRegexFrequency(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    if (parameters.getRevision().getComment() == null ||
        StringUtils.isEmpty(parameters.getRevision().getComment().getValue())) {
      return 0;
    }

    val comment = parameters.getRevision().getComment().getValue();
    val words = WordsExtractor.extractWords(comment);
    val matches = countMatches(this.regularExpressions, words.elementSet());
    return words.size() > 0 ? matches / words.size() : 0;
  }
}

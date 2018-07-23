package features.language;

import static util.RegexUtil.countMatches;

import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureContext;
import org.apache.commons.lang3.StringUtils;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Number of matching words to a list of regex patterns relative to the size of comment size.
 */
public class CommentRegexFrequency implements Feature {

  private final Set<Pattern> regularExpressions;

  CommentRegexFrequency(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public Object getValue(MyRevisionType revision, FeatureContext featureContext) {
    if (revision.getComment() == null || StringUtils.isEmpty(revision.getComment().getValue())) {
      return 0;
    }

    val comment = revision.getComment().getValue();
    val words = WordsExtractor.extractWords(comment);
    val matches = countMatches(this.regularExpressions, words.elementSet());
    return words.size() > 0 ? matches / words.size() : 0;
  }
}

package features.language;

import static util.RegexUtil.countMatches;

import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;
import lombok.var;
import model.FeatureContext;
import util.BasicUtils;
import util.TableContentExtractor;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Percentage increase of matching words to a list of regex patterns compared to previous revision.
 */
class TableRegexImpact implements Feature {

  private final Set<Pattern> regularExpressions;

  TableRegexImpact(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    val content = TableContentExtractor.getContent(revision);
    val words = WordsExtractor.extractWords(content);
    val previousRevision = BasicUtils.getPreviousRevision(featureContext.getPreviousRevisions());
    var previousContent = previousRevision != null
        ? TableContentExtractor.getContent(previousRevision)
        : "";

    val previousWords = WordsExtractor.extractWords(previousContent);
    val previousMatches = countMatches(this.regularExpressions, previousWords.elementSet());
    val matches = countMatches(this.regularExpressions, words.elementSet());
    val previousMatchCount = previousMatches > 0 ? previousMatches : 1;
    return ((float) matches / previousMatchCount);
  }
}

package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.val;
import lombok.var;
import model.FeatureContext;
import util.BasicUtils;
import util.TableContentExtractor;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Percentage increase of added words from word list compared to previous revision.
 */
class TableWordImpact implements Feature {

  private final Set<String> words;

  TableWordImpact(Set<String> words) {
    this.words = words;
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
    val previousMatches = Sets.intersection(previousWords.elementSet(), this.words);
    val matches = Sets.intersection(words.elementSet(), this.words);
    val previousMatchCount = previousMatches.size() > 0 ? previousMatches.size() : 1;
    return ((float) matches.size() / previousMatchCount);
  }
}

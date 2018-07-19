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
 * Number of matching words in word list relative to the size of the edit in tables.
 */
class TableWordFrequency implements Feature {

  private final Set<String> words;

  TableWordFrequency(Set<String> words) {
    this.words = words;
  }

  @Override
  public Object getValue(MyRevisionType revision, FeatureContext featureContext) {
    val previousRevision = BasicUtils.getPreviousRevision(featureContext.getPreviousRevisions());
    var previousContent = previousRevision != null
        ? TableContentExtractor.getContent(previousRevision)
        : "";

    val content = TableContentExtractor.getContent(revision);
    val diffWords = WordsExtractor.diffWords(previousContent, content);
    val matches = Sets.intersection(diffWords.elementSet(), words);
    return diffWords.size() > 0 ? ((float) matches.size() / diffWords.size()) : 0;
  }
}

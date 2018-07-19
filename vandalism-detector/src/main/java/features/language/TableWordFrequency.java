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
  private final boolean isMatching;

  TableWordFrequency(Set<String> words, boolean isMatching) {
    this.words = words;
    this.isMatching = isMatching;
  }

  TableWordFrequency(Set<String> words) {
    this(words, true);
  }

  @Override
  public Object getValue(MyRevisionType revision, FeatureContext featureContext) {
    val previousRevision = BasicUtils.getPreviousRevision(featureContext.getPreviousRevisions());
    var previousContent = previousRevision != null
        ? TableContentExtractor.getContent(previousRevision)
        : "";

    val content = TableContentExtractor.getContent(revision);
    val diffWords = WordsExtractor.diffWords(previousContent, content);
    val matches = getMatches(diffWords.elementSet());
    return diffWords.size() > 0 ? ((float) matches.size() / diffWords.size()) : 0;
  }

  private Set<String> getMatches(Set<String> words) {
    return this.isMatching
        ? Sets.intersection(words, this.words)
        : Sets.difference(words, this.words);
  }
}

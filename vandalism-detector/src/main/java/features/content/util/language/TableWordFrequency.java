package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.val;
import lombok.var;
import model.FeatureParameters;
import util.TableContentExtractor;
import util.WordsExtractor;

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
  public Object getValue(final FeatureParameters parameters) {
    var previousContent = TableContentExtractor.getPreviousContent(parameters);
    val content = TableContentExtractor.getContent(parameters);
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

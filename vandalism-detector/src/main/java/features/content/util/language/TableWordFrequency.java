package features.content.util.language;

import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import features.Feature;
import features.content.util.TableContentExtractor;
import java.util.Set;
import lombok.val;
import model.FeatureParameters;
import util.WordsExtractor;
import util.DiffUtil;

/**
 * Number of matching words in word list relative to the size of the edit in tables.
 */
public class TableWordFrequency implements Feature {

  private final Set<String> words;
  private final boolean isMatching;

  public TableWordFrequency(Set<String> words, boolean isMatching) {
    this.words = words;
    this.isMatching = isMatching;
  }

  public TableWordFrequency(Set<String> words) {
    this(words, true);
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    final Multiset<String> diffWords = DiffUtil.diffWords(parameters);
    val matches = getMatches(diffWords.elementSet());
    return diffWords.size() > 0 ? ((float) matches.size() / diffWords.size()) : 0;
  }

  private Set<String> getMatches(Set<String> words) {
    return this.isMatching
        ? Sets.intersection(words, this.words)
        : Sets.difference(words, this.words);
  }
}

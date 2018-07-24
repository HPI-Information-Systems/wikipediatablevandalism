package features.content.util.language;

import static util.MultisetUtils.getMatches;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
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
  public double getValue(final FeatureParameters parameters) {
    final Multiset<String> diffWords = DiffUtil.diffWords(parameters);
    val matches = getMatches(diffWords, this.words, this.isMatching);
    return diffWords.size() > 0 ? ((double) matches.size() / diffWords.size()) : 0;
  }
}

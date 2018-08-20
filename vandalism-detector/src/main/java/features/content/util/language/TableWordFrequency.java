package features.content.util.language;

import static util.MultisetUtils.getMatches;

import com.google.common.collect.Multiset;
import features.Feature;
import java.util.Set;
import lombok.Builder;
import lombok.val;
import model.FeatureParameters;
import util.DiffUtil;
import util.StemmerUtils;

/**
 * Number of matching words in word list relative to the size of the edit in tables.
 */
@Builder
public class TableWordFrequency implements Feature {

  private final Set<String> words;
  private final boolean isMatching;
  private final boolean stem;

  private TableWordFrequency(Set<String> words, boolean isMatching, boolean stem) {
    this.words = words;
    this.isMatching = isMatching;
    this.stem = stem;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val diffWords = diffWords(parameters);
    val matches = getMatches(diffWords, this.words, this.isMatching);
    return diffWords.size() > 0 ? ((double) matches.size() / diffWords.size()) : 0;
  }

  private Multiset<String> diffWords(final FeatureParameters parameters) {
    val diffWords = DiffUtil.diffWords(parameters);
    return stem ? StemmerUtils.stem(diffWords) : diffWords;
  }

  public static TableWordFrequencyBuilder ofMatchedWords(Set<String> words) {
    return builder().words(words).isMatching(true).stem(true);
  }

  public static TableWordFrequencyBuilder ofUnmatchedWords(Set<String> words) {
    return builder().words(words).isMatching(false).stem(true);
  }
}

package features.content.util.language;

import static util.MultisetUtils.getMatches;

import com.google.common.collect.Multiset;
import features.Feature;
import java.util.Set;
import lombok.Builder;
import lombok.val;
import model.FeatureParameters;
import util.StemmerUtils;
import util.WordsExtractor;

/**
 * Number of added matching words relative to the size of the comment.
 */
@Builder
public class CommentWordFrequency implements Feature {

  private final Set<String> words;
  private final boolean isMatching;
  private final boolean stem;

  private CommentWordFrequency(Set<String> words, boolean isMatching, boolean stem) {
    this.words = words;
    this.isMatching = isMatching;
    this.stem = stem;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val words = commentOf(parameters);
    val matches = getMatches(words, this.words, this.isMatching);
    return words.size() > 0 ? ((double) matches.size() / words.size()) : 0;
  }

  private Multiset<String> commentOf(final FeatureParameters parameters) {
    val words = WordsExtractor.extractWords(parameters.getUserComment());
    return stem ? StemmerUtils.stem(words) : words;
  }

  public static CommentWordFrequencyBuilder ofMatchedWords(final Set<String> words) {
    return builder().words(words).isMatching(true).stem(true);
  }

  public static CommentWordFrequencyBuilder ofUnmatchedWords(final Set<String> words) {
    return builder().words(words).isMatching(false).stem(true);
  }
}

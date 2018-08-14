package features.content.util.language;

import static util.MultisetUtils.getMatches;

import features.Feature;
import java.util.Set;
import lombok.val;
import model.FeatureParameters;
import util.StemmerUtils;
import util.WordsExtractor;

/**
 * Number of added matching words relative to the size of the comment.
 */
public class CommentWordFrequency implements Feature {

  private final Set<String> words;
  private final boolean isMatching;

  public CommentWordFrequency(Set<String> words, boolean isMatching) {
    this.words = words;
    this.isMatching = isMatching;
  }

  public CommentWordFrequency(Set<String> words) {
    this(words, true);
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val comment = parameters.getUserComment();
    val words = StemmerUtils.stem(WordsExtractor.extractWords(comment));
    val matches = getMatches(words, this.words, this.isMatching);
    return words.size() > 0 ? ((double) matches.size() / words.size()) : 0;
  }
}

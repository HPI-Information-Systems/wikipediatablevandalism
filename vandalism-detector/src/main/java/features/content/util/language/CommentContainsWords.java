package features.content.util.language;

import features.Feature;
import java.util.HashSet;
import java.util.Set;
import model.FeatureParameters;
import util.WordsExtractor;

/**
 * Boolean feature to show if a comment mentions a word.
 */
public class CommentContainsWords implements Feature {

  private final Set<String> words;

  public CommentContainsWords(Set<String> words) {
    this.words = words;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    final String comment = parameters.getUserComment();
    final Set<String> commentWords = new HashSet<>(WordsExtractor.extractWords(comment));
    commentWords.retainAll(words);
    return commentWords.isEmpty() ? 0 : 1;
  }
}

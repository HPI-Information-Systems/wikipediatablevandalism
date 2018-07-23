package features.language;

import static util.MultisetUtils.getMatches;

import features.Feature;
import java.util.Set;
import lombok.val;
import model.FeatureParameters;
import org.apache.commons.lang3.StringUtils;
import util.WordsExtractor;

/**
 * Number of added matching words relative to the size of the comment.
 */
class CommentWordFrequency implements Feature {

  private final Set<String> words;
  private final boolean isMatching;

  CommentWordFrequency(Set<String> words, boolean isMatching) {
    this.words = words;
    this.isMatching = isMatching;
  }

  CommentWordFrequency(Set<String> words) {
    this(words, false);
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    if (parameters.getRevision().getComment() == null ||
        StringUtils.isEmpty(parameters.getRevision().getComment().getValue())) {
      return 0;
    }

    val comment = parameters.getRevision().getComment().getValue();
    val words = WordsExtractor.extractWords(comment);
    val matches = getMatches(words, this.words, this.isMatching);
    return words.size() > 0 ? matches.size() / words.size() : 0;
  }
}

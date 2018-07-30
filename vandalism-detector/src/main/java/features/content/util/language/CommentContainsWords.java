package features.content.util.language;

import features.Feature;
import java.util.Set;
import lombok.val;
import model.FeatureParameters;
import org.apache.commons.lang3.StringUtils;

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
    if (parameters.getRevision().getComment() == null ||
        StringUtils.isEmpty(parameters.getRevision().getComment().getValue())) {
      return 0;
    }

    val comment = parameters.getRevision().getComment().getValue();
    val containsWord = words.stream()
        .anyMatch(word -> StringUtils.containsIgnoreCase(comment, word));
    return containsWord ? 1 : 0;
  }
}

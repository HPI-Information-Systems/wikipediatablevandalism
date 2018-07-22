package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.val;
import model.FeatureContext;
import org.apache.commons.lang3.StringUtils;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Boolean feature to show if a comment mentions a word.
 */
class CommentContainsWords implements Feature {

  private final Set<String> words;

  CommentContainsWords(Set<String> words) {
    this.words = words;
  }

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    if (revision.getComment() == null || StringUtils.isEmpty(revision.getComment().getValue())) {
      return 0;
    }

    val comment = revision.getComment().getValue();
    val words = WordsExtractor.extractWords(comment);
    val matches = Sets.intersection(words.elementSet(), this.words);
    return matches.size() > 0 ? 1 : 0;
  }
}

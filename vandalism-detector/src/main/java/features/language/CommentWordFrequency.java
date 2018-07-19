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
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    if (revision.getComment() == null || StringUtils.isEmpty(revision.getComment().getValue())) {
      return 0;
    }

    val comment = revision.getComment().getValue();
    val words = WordsExtractor.extractWords(comment);
    val matches = getMatches(words.elementSet());
    return words.size() > 0 ? matches.size() / words.size() : 0;
  }

  private Set<String> getMatches(Set<String> words) {
    return this.isMatching
        ? Sets.intersection(words, this.words)
        : Sets.difference(words, this.words);
  }
}

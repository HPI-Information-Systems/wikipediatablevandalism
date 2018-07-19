package features.language;

import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.val;
import model.FeatureContext;
import org.apache.commons.lang3.StringUtils;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Number of added 1. & 2. personal pronouns relative to the size of the comment.
 */
public class CommentPronounFrequency implements Feature {

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    if (revision.getComment() == null || StringUtils.isEmpty(revision.getComment().getValue())) {
      return 0;
    }

    val comment = revision.getComment().getValue();
    final Multiset<String> words = WordsExtractor.extractWords(comment);
    final Set<String> pronouns = Sets.intersection(words.elementSet(), PronounWordList.getWords());
    return words.size() > 0 ? pronouns.size() / words.size() : 0;
  }
}

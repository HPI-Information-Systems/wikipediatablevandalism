package features.content.util.language;

import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import features.Feature;
import util.WordsExtractor;
import java.util.Set;
import lombok.val;
import model.FeatureContext;
import org.apache.commons.lang3.StringUtils;
import wikixmlsplit.datastructures.MyRevisionType;

public class OffensiveWordsInComment implements Feature {

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    if (revision.getComment() == null) {
      return 0;
    }

    val comment = revision.getComment().getValue();
    if (StringUtils.isEmpty(comment)) {
      return 0;
    }

    final Multiset<String> words = WordsExtractor.extractWords(comment);
    final Set<String> common = Sets.intersection(words.elementSet(), OffensiveWordList.getWords());
    return common.size();
  }
}

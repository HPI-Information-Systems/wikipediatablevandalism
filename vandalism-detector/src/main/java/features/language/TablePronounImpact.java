package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import lombok.val;
import lombok.var;
import model.FeatureContext;
import util.BasicUtils;
import util.TableContentExtractor;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Percentage increase of added 1. & 2. personal pronouns to previous revision.
 */
class TablePronounImpact implements Feature {

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    val content = TableContentExtractor.getContent(revision);
    val words = WordsExtractor.extractWords(content);
    val previousRevision = BasicUtils.getPreviousRevision(featureContext.getPreviousRevisions());
    var previousContent = previousRevision != null
        ? TableContentExtractor.getContent(previousRevision)
        : "";
    val previousWords = WordsExtractor.extractWords(previousContent);

    val previousPronouns = Sets
        .intersection(previousWords.elementSet(), PronounWordList.getWords());
    val pronouns = Sets.intersection(words.elementSet(), PronounWordList.getWords());
    val previousPronounsCount = previousPronouns.size() > 0 ? previousPronouns.size() : 1;
    return ((float) pronouns.size() / previousPronounsCount);
  }
}

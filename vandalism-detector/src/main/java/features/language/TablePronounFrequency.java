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
 * Number of added 1. & 2. personal pronouns relative to the size of the edit in tables.
 */
public class TablePronounFrequency implements Feature {

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    val previousRevision = BasicUtils.getPreviousRevision(featureContext.getPreviousRevisions());
    var previousContent = previousRevision != null
        ? TableContentExtractor.getContent(previousRevision)
        : "";

    val content = TableContentExtractor.getContent(revision);
    val diffWords = WordsExtractor.diffWords(previousContent, content);
    val personalPronouns = Sets.intersection(diffWords.elementSet(), PronounWordList.getWords());
    return diffWords.size() > 0 ? ((float) personalPronouns.size() / diffWords.size()) : 0;
  }
}

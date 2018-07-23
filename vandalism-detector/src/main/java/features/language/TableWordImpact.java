package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.val;
import lombok.var;
import model.FeatureParameters;
import util.BasicUtils;
import util.TableContentExtractor;
import util.WordsExtractor;

/**
 * Percentage increase of added words from word list compared to previous revision.
 */
class TableWordImpact implements Feature {

  private final Set<String> words;
  private final boolean isMatching;

  TableWordImpact(Set<String> words, boolean isMatching) {
    this.words = words;
    this.isMatching = isMatching;
  }

  TableWordImpact(Set<String> words) {
    this(words, false);
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    val content = TableContentExtractor.getContent(parameters);
    val words = WordsExtractor.extractWords(content);
    val previousRevision = BasicUtils.getPreviousRevision(parameters.getPreviousRevisions());
    var previousContent = previousRevision != null
        ? TableContentExtractor.getContent(previousRevision)
        : "";

    val previousWords = WordsExtractor.extractWords(previousContent);
    val previousMatches = getMatches(previousWords.elementSet());
    val matches = getMatches(words.elementSet());
    val previousMatchCount = previousMatches.size() > 0 ? previousMatches.size() : 1;
    return ((float) matches.size() / previousMatchCount);
  }

  private Set<String> getMatches(Set<String> words) {
    return this.isMatching
        ? Sets.intersection(words, this.words)
        : Sets.difference(words, this.words);
  }
}

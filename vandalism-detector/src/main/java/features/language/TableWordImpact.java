package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
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
    val words = WordsExtractor.extractWords(BasicUtils.getCurrentTables(parameters));
    val previousWords = WordsExtractor.extractWords(BasicUtils.getPreviousTables(parameters));

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

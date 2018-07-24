package features.content.util.language;

import static util.MultisetUtils.getMatches;

import features.Feature;
import features.content.util.TableContentExtractor;
import java.util.Set;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import util.WordsExtractor;

/**
 * Percentage increase of added words from word list compared to previous revision.
 */
public class TableWordImpact implements Feature {

  private final Set<String> words;
  private final boolean isMatching;

  public TableWordImpact(Set<String> words, boolean isMatching) {
    this.words = words;
    this.isMatching = isMatching;
  }

  public TableWordImpact(Set<String> words) {
    this(words, false);
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val words = WordsExtractor.extractWords(BasicUtils.getCurrentTables(parameters));
    val previousWords = WordsExtractor.extractWords(BasicUtils.getPreviousTables(parameters));

    val previousMatches = getMatches(previousWords, this.words, this.isMatching);
    val matches = getMatches(words, this.words, this.isMatching);
    val previousMatchCount = previousMatches.size() > 0 ? previousMatches.size() : 1;
    return ((double) (matches.size() - previousMatchCount) / previousMatchCount);
  }
}

package features.content.util.language;

import static util.MultisetUtils.getMatches;

import com.google.common.collect.Multiset;
import features.Feature;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import util.StemmerUtils;
import util.WordsExtractor;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Percentage increase of added words from word list compared to previous revision.
 */
@Builder
public class TableWordImpact implements Feature {

  private final Set<String> words;
  private final boolean isMatching;
  private final boolean stem;

  private TableWordImpact(Set<String> words, boolean isMatching, boolean stem) {
    this.words = words;
    this.isMatching = isMatching;
    this.stem = stem;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val words = wordsOf(BasicUtils.getCurrentChangedTables(parameters));
    val previousWords = wordsOf(BasicUtils.getPreviousChangedTables(parameters));

    val previousMatches = getMatches(previousWords, this.words, this.isMatching);
    val matches = getMatches(words, this.words, this.isMatching);
    val previousMatchCount = previousMatches.size() > 0 ? previousMatches.size() : 1;
    return ((double) (matches.size() - previousMatches.size()) / previousMatchCount);
  }

  private Multiset<String> wordsOf(final List<WikiTable> tables) {
    val words = WordsExtractor.extractWords(tables);
    return stem ? StemmerUtils.stem(words) : words;
  }

  public static TableWordImpactBuilder ofMatchedWords(Set<String> words) {
    return builder().words(words).isMatching(true).stem(true);
  }

  public static TableWordImpactBuilder ofUnmatchedWords(Set<String> words) {
    return builder().words(words).isMatching(false).stem(true);
  }
}

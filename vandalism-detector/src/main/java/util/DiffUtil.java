package util;

import static util.CellExtractor.extractCells;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multiset;
import java.util.Collection;
import java.util.List;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class DiffUtil {

  public static Multiset<String> diffWords(final FeatureParameters parameters) {
    return diffWords(
        BasicUtils.getPreviousTables(parameters),
        BasicUtils.getCurrentTables(parameters)
    );
  }

  public static Multiset<String> diffTokens(final FeatureParameters parameters) {
    return diffTokens(
        BasicUtils.getPreviousTables(parameters),
        BasicUtils.getCurrentTables(parameters)
    );
  }

  public static Multiset<Cell> diffCells(final FeatureParameters parameters) {
    return diffCells(
        BasicUtils.getPreviousTables(parameters),
        BasicUtils.getCurrentTables(parameters)
    );
  }


  @VisibleForTesting
  static Multiset<String> diffWords(final Collection<WikiTable> previous,
      final Collection<WikiTable> current) {

    final Multiset<String> previousWords = WordsExtractor.extractWords(previous);
    final Multiset<String> words = WordsExtractor.extractWords(current);
    words.removeAll(previousWords);
    return words;
  }

  @VisibleForTesting
  static Multiset<String> diffTokens(final Collection<WikiTable> previous,
      final Collection<WikiTable> current) {

    final Multiset<String> previousTokens = TokenExtractor.extractTokens(previous);
    final Multiset<String> tokens = TokenExtractor.extractTokens(current);
    tokens.removeAll(previousTokens);
    return tokens;
  }

  public static Multiset<String> diffWords(final String previousText, final String text) {
    Multiset<String> previousWords = WordsExtractor.extractWords(previousText);
    Multiset<String> words = WordsExtractor.extractWords(text);
    words.removeAll(previousWords);
    return words;
  }

  public static Multiset<Cell> diffCells(List<WikiTable> previousTables,
      List<WikiTable> currentTables) {

    Multiset<Cell> previousCells = extractCells(previousTables);
    Multiset<Cell> cells = extractCells(currentTables);
    cells.removeAll(previousCells);
    return cells;
  }
}

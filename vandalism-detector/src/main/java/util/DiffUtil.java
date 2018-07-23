package util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multiset;
import java.util.Collection;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class DiffUtil {

  public static Multiset<String> diffWords(final FeatureParameters parameters) {
    return diffWords(
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

  public static Multiset<String> diffWords(final String previousText, final String text) {
    Multiset<String> previousWords = WordsExtractor.extractWords(previousText);
    Multiset<String> words = WordsExtractor.extractWords(text);
    words.removeAll(previousWords);
    return words;
  }
}

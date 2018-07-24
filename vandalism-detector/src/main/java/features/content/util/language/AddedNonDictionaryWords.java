package features.content.util.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.Builder;
import lombok.Value;
import matching.table.TableMatch;
import model.FeatureParameters;
import util.WordsExtractor;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class AddedNonDictionaryWords implements Feature {

  @Override
  public Object getValue(final FeatureParameters parameters) {
    final TableMatch match = parameters.getRelevantMatch();
    if (match == null) {
      return 0;
    }

    final WordCount current = dictionaryWordCount(match.getCurrentTable());
    final WordCount previous = dictionaryWordCount(match.getPreviousTable());
    return current.getNonDictionaryWordCount() - previous.getNonDictionaryWordCount();
  }

  private WordCount dictionaryWordCount(final WikiTable table) {
    final Set<String> words = WordsExtractor.extractWords(table).elementSet();
    final Set<String> knownWords = DictionaryWordList.getWords();
    final int dictionaryWordCount = Sets.intersection(words, knownWords).size();

    return WordCount.builder()
        .wordCount(words.size())
        .dictionaryWordCount(dictionaryWordCount)
        .nonDictionaryWordCount(words.size() - dictionaryWordCount)
        .build();
  }

  @Value
  @Builder
  private static class WordCount {

    private int wordCount;
    private int dictionaryWordCount;
    private int nonDictionaryWordCount;
  }
}

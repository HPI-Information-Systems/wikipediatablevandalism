package features.language;

import com.google.common.collect.Sets;
import features.Feature;
import java.util.Set;
import lombok.Builder;
import lombok.Value;
import matching.table.TableMatch;
import model.FeatureContext;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

class AddedNonDictionaryWords implements Feature {


  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    final TableMatch match = featureContext.getRelevantMatch();
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

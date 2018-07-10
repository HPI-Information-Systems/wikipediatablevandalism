package features.content;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import features.Feature;
import java.util.List;
import java.util.Set;
import matching.table.TableMatch;
import model.FeatureContext;
import util.WordsExtractor;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

class OffensiveWordsInTable implements Feature {

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext featureContext) {
    int totalOffensiveWords = 0;

    for (final WikiTable added : featureContext.getResult().getAddedTables()) {
      totalOffensiveWords += getOffensiveWordCount(added);
    }

    for (final TableMatch changed : featureContext.getResult().getMatches()) {
      totalOffensiveWords += getOffensiveWordCount(changed.getCurrentTable());
    }

    return totalOffensiveWords;
  }

  private int getOffensiveWordCount(final WikiTable table) {
    final List<Cell> cells = table.getRows().stream()
        .flatMap(row -> row.getValues().stream())
        .collect(toList());

    final Multiset<String> words = WordsExtractor.extractWords(cells);
    final Set<String> common = Sets.intersection(words.elementSet(), OffensiveWordList.getWords());
    return common.size();
  }
}

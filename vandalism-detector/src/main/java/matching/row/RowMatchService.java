package matching.row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.val;
import util.WordsExtractor;
import wikixmlsplit.matching.similarity.BagOfWordsSimiliarty;
import wikixmlsplit.renderer.wikitable.Row;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class RowMatchService {

  private static final double SIMILARITY_THRESHOLD = 0.25;

  private final BagOfWordsSimiliarty similarity;

  public RowMatchService() {
    similarity = new BagOfWordsSimiliarty(true);
  }

  public RowMatchResult matchRows(final WikiTable previous, final WikiTable current) {
    val result = RowMatchResult.builder();

    for (final Row row : previous.getRows()) {
      final RowMatch match = getBestMatch(row, current.getRows());
      if (match != null && match.getSimilarity() >= SIMILARITY_THRESHOLD) {
        result.match(match);
      }
    }

    return result.build();
  }

  // Java port inspired by key_discovery.data.column.matching.TobiasMatchinStrategy#getOrderedMatches
  private RowMatch getBestMatch(final Row toCompare, final List<Row> toMatch) {
    if (toMatch.isEmpty()) {
      return null;
    }

    val wordsToCompare = WordsExtractor.extractWords(toCompare.getValues());
    final List<RowMatch> similarities = new ArrayList<>();

    for (int index = 0; index < toMatch.size(); ++index) {
      val words = WordsExtractor.extractWords(toMatch.get(index).getValues());
      val similarityValue = similarity.getSimilarity(wordsToCompare, words);
      similarities.add(RowMatch.builder().matchedIndex(index).similarity(similarityValue).build());
    }

    return Collections.max(similarities, bySimilarity());
  }

  private Comparator<RowMatch> bySimilarity() {
    return Comparator.comparing(RowMatch::getSimilarity);
  }
}

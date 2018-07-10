package util;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.val;
import wikixmlsplit.renderer.wikitable.Cell;

/**
 * Java port of key_discovery.data.column.matching.TobiasMatchinStrategy#extractWords
 */
public class WordsExtractor {

  private static final Pattern WHITESPACE = Pattern.compile("\\s+");
  private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\p{L}\\p{M} 0-9]");

  public static Multiset<String> extractWords(final List<Cell> cells) {
    val normalizedWords = cells.stream()
        .map(Cell::getValue)
        .flatMap(WordsExtractor::wordsOf)
        .map(WordsExtractor::normalize)
        .collect(toList());

    return HashMultiset.create(normalizedWords);
  }

  private static Stream<String> wordsOf(final String value) {
    return WHITESPACE.splitAsStream(value);
  }

  private static String normalize(final String value) {
    return NON_ALPHANUMERIC.matcher(value).replaceAll("").toLowerCase();
  }
}
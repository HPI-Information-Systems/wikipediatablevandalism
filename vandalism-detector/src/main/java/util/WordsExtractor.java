package util;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.Row;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Java port of key_discovery.data.column.matching.TobiasMatchinStrategy#extractWords
 */
public class WordsExtractor {

  private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\p{L}\\p{M}0-9]");

  public static Multiset<String> extractWords(final Collection<WikiTable> tables) {
    final Multiset<String> words = HashMultiset.create();
    for (final WikiTable table : tables) {
      words.addAll(extractWords(table));
    }
    return words;
  }

  public static Multiset<String> extractWords(final WikiTable table) {
    final Multiset<String> words = HashMultiset.create(
        AttributeUtil.extractNonStandardAttributeNames(table.attributes));

    for (final Row row : table.getRows()) {
      words.addAll(AttributeUtil.extractNonStandardAttributeNames(row.getAttributes()));
      words.addAll(extractWords(row.getValues()));
    }

    return words;
  }

  public static Multiset<String> extractWords(final List<Cell> cells) {
    val normalizedWords = cells.stream()
        .flatMap(WordsExtractor::contentOf)
        .flatMap(WordsExtractor::wordsOf)
        .map(WordsExtractor::normalize)
        .filter(s -> !StringUtils.isBlank(s))
        .collect(toList());

    return HashMultiset.create(normalizedWords);
  }

  public static Multiset<String> extractWords(final String text) {
    val normalizedWords = wordsOf(text)
        .map(WordsExtractor::normalize)
        .filter(s -> !StringUtils.isBlank(s))
        .collect(toList());
    return HashMultiset.create(normalizedWords);
  }

  private static Stream<String> contentOf(final Cell cell) {
    final List<String> content = new ArrayList<>();
    content.add(cell.getValue());
    content.addAll(AttributeUtil.extractNonStandardAttributeNames(cell.getAttributes()));
    return content.stream();
  }

  private static Stream<String> wordsOf(final String value) {
    return NON_ALPHANUMERIC.splitAsStream(value);
  }

  private static String normalize(final String value) {
    return NON_ALPHANUMERIC.matcher(value).replaceAll("").toLowerCase();
  }
}

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

public class TokenExtractor {

  private static final Pattern WHITESPACE = Pattern.compile("\\s+");

  public static Multiset<String> extractTokens(final Collection<WikiTable> tables) {
    final Multiset<String> words = HashMultiset.create();
    for (final WikiTable table : tables) {
      words.addAll(extractTokens(table));
    }
    return words;
  }

  public static Multiset<String> extractTokens(final WikiTable table) {
    final Multiset<String> words = HashMultiset.create(
        AttributeUtil.extractNonStandardAttributeNames(table.attributes));

    for (final Row row : table.getRows()) {
      words.addAll(AttributeUtil.extractNonStandardAttributeNames(row.getAttributes()));
      words.addAll(extractTokens(row.getValues()));
    }

    return words;
  }

  public static Multiset<String> extractTokens(final List<Cell> cells) {
    val normalizedWords = cells.stream()
        .flatMap(TokenExtractor::contentOf)
        .flatMap(TokenExtractor::wordsOf)
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
    return WHITESPACE.splitAsStream(value);
  }
}

package features.content.util;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import util.AttributeUtil;
import wikixmlsplit.renderer.wikitable.Attribute;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Create a string representation of a set of tables for use with text-based content features.
 */
public class TableContentExtractor {

  private static final String SEP = " ";

  /**
   * All text features should be able to capture all user-generated content within the current
   * revision. For instance character ratios: they operate on textual additions. Replicating all
   * character ratios once for content and once for comment is probably not meaningful.
   */
  @Nonnull
  public static String getContentWithComment(final String comment, final List<WikiTable> tables) {
    return extractToString(comment, tables);
  }

  @Nonnull
  public static String getContent(final List<WikiTable> tables) {
    return extractToString(null, tables);
  }

  private static String extractToString(@Nullable final String comment,
      final List<WikiTable> tables) {

    final StringBuilder content = new StringBuilder(128);

    if (comment != null) {
      content.append(comment).append(SEP);
    }

    writeTableContents(content, tables);
    return content.toString();
  }

  private static void writeTableContents(final StringBuilder content,
      final List<WikiTable> tables) {

    tables.stream()

        // Table
        .peek(table -> writeAttributes(content, table.attributes))

        // Row
        .flatMap(table -> table.getRows().stream())
        .peek(row -> writeAttributes(content, row.getAttributes()))

        // Cell
        .flatMap(row -> row.getValues().stream())
        .peek(cell -> writeAttributes(content, cell.getAttributes()))
        .forEach(cell -> content.append(cell.getValue()).append(SEP));
  }

  private static void writeAttributes(final StringBuilder content,
      final Set<Attribute> attributes) {

    for (final Attribute attribute : AttributeUtil.filterNonStandardAttributes(attributes)) {
      content.append(attribute.getKey()).append(SEP);
      if (attribute.getValue() != null) {
        content.append(attribute.getValue()).append(SEP);
      }
    }
  }
}

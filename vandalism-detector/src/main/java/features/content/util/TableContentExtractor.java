package features.content.util;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.CommentType;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.Attribute;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Create a string representation of a set of tables for use with text-based content features.
 */
public class TableContentExtractor {

  private static final String SEP = " ";

  @Nonnull
  public static String getContent(final FeatureParameters parameters) {
    return extractToString(parameters.getRevision(), BasicUtils.getCurrentTables(parameters));
  }

  @Nonnull
  public static String getPreviousContent(final FeatureParameters parameters) {
    return extractToString(parameters.getPreviousRevision(),
        BasicUtils.getPreviousTables(parameters));
  }

  private static String extractToString(@Nullable final MyRevisionType revision,
      final List<WikiTable> tables) {

    final StringBuilder content = new StringBuilder(128);

    if (revision != null) {
      writeCommentContent(content, revision.getComment());
    }

    writeTableContents(content, tables);
    return content.toString();
  }

  private static void writeCommentContent(final StringBuilder content, final CommentType comment) {
    if (comment == null || comment.getValue() == null) {
      return;
    }
    content.append(comment.getValue()).append(SEP);
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

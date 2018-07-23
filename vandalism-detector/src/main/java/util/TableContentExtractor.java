package util;

import java.util.List;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.CommentType;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class TableContentExtractor {

  public static String getContent(final FeatureParameters parameters) {
    return extract(parameters.getRevision(), BasicUtils.getCurrentTables(parameters));
  }

  public static String getPreviousContent(final FeatureParameters parameters) {
    return extract(parameters.getPreviousRevision(), BasicUtils.getPreviousTables(parameters));
  }

  private static String extract(final MyRevisionType revision,
      final List<WikiTable> tables) {

    final StringBuilder content = new StringBuilder(128);

    if (revision != null) {
      getCommentContent(content, revision.getComment());
    }

    getTableContents(content, tables);
    return content.toString();
  }

  private static void getCommentContent(final StringBuilder content, final CommentType comment) {
    if (comment == null || comment.getValue() == null) {
      return;
    }
    content.append(comment.getValue());
  }

  private static void getTableContents(final StringBuilder content,
      final List<WikiTable> tables) {

    for (final WikiTable table : tables) {
      for (final String word : WordsExtractor.extractWords(table)) {
        content.append(word);
      }
    }
  }
}

package features.content.util.table;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import model.FeatureParameters;
import util.RatioUtil;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.Row;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Model the average size per cell and its changes. Read: on average, the content of a cell has a
 * size of X bytes. Syntax errors (forgetting to close a table) may cause the remainder of the page
 * to be merged into the last cell, thus disproportionally increasing the average cell size.
 */
public class AverageCellSizeChange {

  private static final Charset CHARSET = StandardCharsets.UTF_8;

  public static double averageCellSizeIncrease(FeatureParameters parameters) {
    return parameters.getMatch().map(m -> {
      double previous = averageCellSize(m.getPreviousTable());
      double current = averageCellSize(m.getCurrentTable());
      return RatioUtil.added(previous, current);
    }).orElse(0.);
  }

  public static double averageCellSizeDecrease(FeatureParameters parameters) {
    return parameters.getMatch().map(m -> {
      double previous = averageCellSize(m.getPreviousTable());
      double current = averageCellSize(m.getCurrentTable());
      return RatioUtil.removed(previous, current);
    }).orElse(0.);
  }

  public static double averageCellSize(FeatureParameters parameters) {
    return parameters.getMatch().map(m -> averageCellSize(m.getCurrentTable())).orElse(0.);
  }

  private static double averageCellSize(final WikiTable table) {
    double size = getSize(table);
    double cellCount = cellCount(table);
    return cellCount == 0 ? 0 : size / cellCount;
  }

  private static double getSize(final WikiTable table) {
    double size = 0;
    for (final Row row : table.getRows()) {
      for (final Cell cell : row.getValues()) {
        size += cell.getValue().getBytes(CHARSET).length;
      }
    }
    return size;
  }

  private static double cellCount(final WikiTable table) {
    return table.getRows().size() * table.getColumns().size();
  }
}

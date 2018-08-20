package features.content.util.table;

import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import util.RatioUtil;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class EmptyCellChange {

  static final Pattern EMPTY_CELL_PATTERN = Pattern.compile("TBA|-|\\s*");

  public static double addedRatio(FeatureParameters parameters) {
    if (parameters.hasRelevantMatch()) {
      val match = requireNonNull(parameters.getRelevantMatch());
      double previousEmptyCellCount = getEmptyCellCount(match.getPreviousTable());
      double currentEmptyCellCount = getEmptyCellCount(match.getCurrentTable());
      return RatioUtil.added(previousEmptyCellCount, currentEmptyCellCount);
    }

    return 0;
  }

  public static double removeRatio(FeatureParameters parameters) {
    if (parameters.hasRelevantMatch()) {
      val match = requireNonNull(parameters.getRelevantMatch());
      double previousEmptyCellCount = getEmptyCellCount(match.getPreviousTable());
      double currentEmptyCellCount = getEmptyCellCount(match.getCurrentTable());
      return RatioUtil.removed(previousEmptyCellCount, currentEmptyCellCount);
    }

    return 0;
  }

  static public double getCount(FeatureParameters parameters) {
    if (parameters.getRelevantMatch() != null) {
      return getEmptyCellCount(parameters.getRelevantMatch().getCurrentTable());
    }
    return 0;
  }

  static private double getEmptyCellCount(WikiTable table) {
    double emptyCellCount = 0;
    for (val row : table.getRows()) {
      for (val cell : row.getValues()) {
        final Matcher matcher = EMPTY_CELL_PATTERN.matcher(cell.getValue());
        if (matcher.matches()) {
          ++emptyCellCount;
        }
      }
    }
    return emptyCellCount;
  }

}

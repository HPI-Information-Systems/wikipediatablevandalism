package features.content.util.table;

import features.Feature;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class EmptyCellChangeRatio implements Feature {

  static final Pattern EMPTY_CELL_PATTERN = Pattern.compile("TBA|-|\\s*");

  @Override
  public double getValue(FeatureParameters parameters) {
    double currentEmptyCellCount = 0;
    double previousEmptyCellCount = 0;
    if (parameters.getRelevantMatch() != null) {
      currentEmptyCellCount = getEmptyCellCount(parameters.getRelevantMatch().getCurrentTable());
      previousEmptyCellCount = getEmptyCellCount(parameters.getRelevantMatch().getPreviousTable());
    }
    if (previousEmptyCellCount == 0) {
      if (currentEmptyCellCount == 0) {
        return 0;
      }
      return -1;
    }
    return (previousEmptyCellCount - currentEmptyCellCount) / previousEmptyCellCount;
  }

  private double getEmptyCellCount(WikiTable table) {
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

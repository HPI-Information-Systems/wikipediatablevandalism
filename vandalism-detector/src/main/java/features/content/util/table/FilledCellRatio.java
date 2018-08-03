package features.content.util.table;

import features.Feature;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.val;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class FilledCellRatio implements Feature {

  static final Pattern EMPTY_CELL_PATTERN = Pattern.compile("TBA|-|\\s*");

  @Override
  public double getValue(FeatureParameters parameters) {
    val actualTables = new ArrayList<WikiTable>(parameters.getResult().getAddedTables());
    for (val table : parameters.getResult().getMatches()) {
      actualTables.add(table.getCurrentTable());
    }

    double cellCount = 0;
    int emptyCellCount = 0;
    for (val table : actualTables) {
      for (val row : table.getRows()) {
        for (val cell : row.getValues()) {
          final Matcher matcher = EMPTY_CELL_PATTERN.matcher(cell.getValue());
          if (matcher.matches()) {
            ++emptyCellCount;
          }
          ++cellCount;
        }
      }
    }

    if (cellCount == 0) {
      return 1;
    }

    return (cellCount - emptyCellCount) / cellCount;
  }

}

package features.content.util.table;

import com.google.common.collect.Sets;
import com.google.common.math.Stats;
import features.Feature;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.val;
import matching.table.TableMatch;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class SharedCellRatio implements Feature {


  @Override
  public double getValue(final FeatureParameters parameters) {
    final List<Double> values = new ArrayList<>();
    for (val match : parameters.getResult().getMatches()) {
      values.add(processMatch(match));
    }

    if (values.isEmpty()) {
      return 1d;
    }

    return Stats.meanOf(values);
  }

  private double processMatch(final TableMatch match) {
    val currentCellValues = getCellValues(match.getCurrentTable());

    if (currentCellValues.isEmpty()) {
      return 1d;
    }

    val previousCellValues = getCellValues(match.getPreviousTable());
    val sharedValues = Sets.intersection(currentCellValues, previousCellValues);
    return (double) sharedValues.size() / currentCellValues.size();
  }

  private Set<String> getCellValues(final WikiTable table) {
    final Set<String> values = new HashSet<>();
    for (val row : table.getRows()) {
      for (int cell = 0; cell < row.getSize(); ++cell) {
        values.add(row.getValue(cell));
      }
    }
    return values;
  }
}

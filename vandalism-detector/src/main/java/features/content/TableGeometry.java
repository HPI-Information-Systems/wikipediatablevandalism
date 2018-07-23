package features.content;

import features.Feature;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.val;
import matching.table.TableMatch;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.WikiTable;

class TableGeometry implements Feature {

  @Getter
  enum Measure {
    Columns(table -> table.getColumns().size()),
    Rows(table -> table.getRows().size()),
    Product(table -> table.getRows().size() * table.getColumns().size());

    private final ValueFunction f;

    Measure(final ValueFunction f) {
      this.f = f;
    }
  }

  @FunctionalInterface
  private interface ValueFunction {

    double apply(WikiTable table);
  }

  private final ValueFunction valueFunction;

  TableGeometry(final Measure measure) {
    valueFunction = measure.getF();
  }

  @Override
  public Object getValue(final FeatureParameters parameters) {
    val precursors = parameters.getPreviousRevisions();

    if (precursors.isEmpty()) {
      return 0;
    }

    return computeSizeChange(parameters);
  }

  private double computeSizeChange(final FeatureParameters context) {
    val results = context.getResult();

    // Matches
    final List<Double> sizeChanges = new ArrayList<>(results.getMatches().size());
    for (val result : results.getMatches()) {
      sizeChanges.add(getSizeChange(result));
    }

    // Additions
    for (val addedTable : results.getAddedTables()) {
      sizeChanges.add(valueFunction.apply(addedTable));
    }

    // Deletions
    for (val removedTable : results.getRemovedTables()) {
      sizeChanges.add(-valueFunction.apply(removedTable));
    }

    double result = 0;
    for (val d : sizeChanges) {
      result += d;
    }
    return result;
  }

  private double getSizeChange(final TableMatch match) {
    val d1 = valueFunction.apply(match.getPreviousTable());
    val d2 = valueFunction.apply(match.getCurrentTable());
    return d2 - d1;
  }
}

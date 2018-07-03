package features.content;

import features.Feature;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.val;
import matching.Match;
import matching.MatchService;
import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class TableGeometry implements Feature {

  @Getter
  public enum Measure {
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

  private final MatchService matchService;
  private final ValueFunction valueFunction;

  TableGeometry(final MatchService matchService, final Measure measure) {
    this.matchService = matchService;
    valueFunction = measure.getF();
  }

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext context) {
    val precursors = context.getPreviousRevisions();

    if (precursors.isEmpty()) {
      return 0;
    }

    return computeSizeChange(revision, context);
  }

  private double computeSizeChange(final MyRevisionType revision, final FeatureContext context) {
    val results = matchService.getMatchingTable(context.getMatching(), revision);

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

  private double getSizeChange(final Match match) {
    val d1 = valueFunction.apply(match.getPreviousTable());
    val d2 = valueFunction.apply(match.getCurrentTable());
    return d2 - d1;
  }
}

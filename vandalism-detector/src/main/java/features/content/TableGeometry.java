package features.content;

import features.Feature;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import matching.MatchService;
import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

@RequiredArgsConstructor
public class TableGeometry implements Feature {

  private final MatchService matchService;

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext context) {
    val precursors = context.getPreviousRevisions();

    if (precursors.isEmpty()) {
      return 0;
    }

    return computeSizeChange(revision, context);
  }

  private double computeSizeChange(final MyRevisionType revision, final FeatureContext context) {
    // Think more.
    // In the context of deletions, this feature does not make much sense.
    // Most probably the single table of the page is gone. It yields score 0, since it is not part
    // of the result of match service. Actually a value like
    // getDimension(unmatchedTableOfPreviousRevision) * -1
    // seems more appropriate
    val results = matchService
        .getMatchingTable(context.getMatching(), revision, context.getPreviousRevisions());

    // Matches
    final List<Double> sizeChanges = new ArrayList<>(results.getMatches().size());
    for (val result : results.getMatches()) {
      sizeChanges.add(getSizeChange(result));
    }

    // Additions
    for (val addedTable : results.getAddedTables()) {
      sizeChanges.add((double) getDimension(addedTable));
    }

    // Deletions
    for (val removedTable : results.getRemovedTables()) {
      sizeChanges.add((double) -getDimension(removedTable));
    }

    double result = 0;
    for (val d : sizeChanges) {
      result += d;
    }
    return result;
  }

  private double getSizeChange(final MatchService.Match match) {
    val d1 = getDimension(match.getPreviousTable());
    val d2 = getDimension(match.getCurrentTable());
    return d2 - d1;
  }

  private int getDimension(final WikiTable table) {
    return table.getColumns().size() * table.getRows().size();
  }
}

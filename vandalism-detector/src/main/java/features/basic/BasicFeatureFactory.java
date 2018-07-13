package features.basic;

import lombok.RequiredArgsConstructor;
import lombok.val;
import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;

@RequiredArgsConstructor
class BasicFeatureFactory {

  Object rowCount(final MyRevisionType revision, final FeatureContext context) {
    val match = context.getRelevantMatch();
    return match == null ? 0 : match.getCurrentTable().getRows().size();
  }

  Object columnCount(final MyRevisionType revision, final FeatureContext context) {
    val match = context.getRelevantMatch();
    return match == null ? 0 : match.getCurrentTable().getColumns().size();
  }

  Object cellCount(final MyRevisionType revision, final FeatureContext context) {
    val match = context.getRelevantMatch();
    if (match == null) {
      return 0;
    }

    val rows = match.getCurrentTable().getRows().size();
    val columns = match.getCurrentTable().getColumns().size();
    return rows * columns;
  }

  Object unmatchedTables(final MyRevisionType revision, final FeatureContext context) {
    if (revision.getParsed() == null) {
      return 1d;
    }

    final double removedTableCount = context.getResult().getRemovedTables().size();
    final double currentTableCount = revision.getParsed().size();
    return (currentTableCount - removedTableCount) / currentTableCount;
  }

  Object unmatchedRows(final MyRevisionType revision, final FeatureContext context) {
    if (context.getRowMatchResult() == null) {
      return 1d;
    }

    final double matchedRowCount = context.getRowMatchResult().getMatches().size();
    final double totalRowCount = context.getRelevantMatch().getCurrentTable().getRows().size();

    if (totalRowCount == 0) {
      return 0;
    }

    return (totalRowCount - matchedRowCount) / totalRowCount;
  }
}

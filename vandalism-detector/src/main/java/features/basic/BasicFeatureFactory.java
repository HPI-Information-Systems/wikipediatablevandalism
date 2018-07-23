package features.basic;

import lombok.RequiredArgsConstructor;
import lombok.val;
import model.FeatureParameters;

@RequiredArgsConstructor
class BasicFeatureFactory {

  Object rowCount(final FeatureParameters parameters) {
    val match = parameters.getRelevantMatch();
    return match == null ? 0 : match.getCurrentTable().getRows().size();
  }

  Object columnCount(final FeatureParameters parameters) {
    val match = parameters.getRelevantMatch();
    return match == null ? 0 : match.getCurrentTable().getColumns().size();
  }

  Object cellCount(final FeatureParameters parameters) {
    val match = parameters.getRelevantMatch();
    if (match == null) {
      return 0;
    }

    val rows = match.getCurrentTable().getRows().size();
    val columns = match.getCurrentTable().getColumns().size();
    return rows * columns;
  }

  Object unmatchedTables(final FeatureParameters parameters) {
    val revision = parameters.getRevision();
    if (revision.getParsed() == null) {
      return 1d;
    }

    final double removedTableCount = parameters.getResult().getRemovedTables().size();
    final double currentTableCount = revision.getParsed().size();
    return (currentTableCount - removedTableCount) / currentTableCount;
  }

  Object unmatchedRows(final FeatureParameters parameters) {
    if (parameters.getRowMatchResult() == null) {
      return 1d;
    }

    final double matchedRowCount = parameters.getRowMatchResult().getMatches().size();
    final double totalRowCount = parameters.getRelevantMatch().getCurrentTable().getRows().size();

    if (totalRowCount == 0) {
      return 0;
    }

    return (totalRowCount - matchedRowCount) / totalRowCount;
  }
}

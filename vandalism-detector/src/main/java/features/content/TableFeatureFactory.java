package features.content;

import features.Feature;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
import features.content.util.table.TableGeometry;
import features.content.util.table.TableGeometry.Measure;
import lombok.val;

class TableFeatureFactory {

  Feature currentRowCount() {
    return parameters -> {
      val match = parameters.getRelevantMatch();
      return match == null ? 0 : match.getCurrentTable().getRows().size();
    };
  }

  Feature currentColumnCount() {
    return parameters -> {
      val match = parameters.getRelevantMatch();
      return match == null ? 0 : match.getCurrentTable().getColumns().size();
    };
  }

  Feature currentCellCount() {
    return parameters -> {
      val match = parameters.getRelevantMatch();
      if (match == null) {
        return 0;
      }

      val rows = match.getCurrentTable().getRows().size();
      val columns = match.getCurrentTable().getColumns().size();
      return rows * columns;
    };
  }

  // down here - table matching required

  Feature unmatchedTables() {
    return parameters -> {
      if (parameters.getRevision().getParsed() == null) {
        return 1d;
      }

      final double removedTableCount = parameters.getResult().getRemovedTables().size();
      final double currentTableCount = parameters.getRevision().getParsed().size();
      return (currentTableCount - removedTableCount) / currentTableCount;
    };
  }

  Feature unmatchedRows() {
    return parameters -> {
      if (parameters.getRowMatchResult() == null) {
        return 1d;
      }

      final double matchedRowCount = parameters.getRowMatchResult().getMatches().size();
      final double totalRowCount = parameters.getRelevantMatch().getCurrentTable().getRows().size();

      if (totalRowCount == 0) {
        return 0;
      }

      return (totalRowCount - matchedRowCount) / totalRowCount;
    };
  }

  Feature cellCount() {
    return new TableGeometry(Measure.Product);
  }

  Feature columnCount() {
    return new TableGeometry(Measure.Columns);
  }

  Feature rowCount() {
    return new TableGeometry(Measure.Rows);
  }

  Feature sharedCellRatio() {
    return new SharedCellRatio();
  }

  Feature rankChange() {
    return new RankChange();
  }

}
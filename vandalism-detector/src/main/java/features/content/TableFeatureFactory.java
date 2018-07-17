package features.content;

import features.Feature;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
import features.content.util.table.TableGeometry;
import features.content.util.table.TableGeometry.Measure;
import lombok.val;

class TableFeatureFactory {

  Feature currentRowCount() {
    return (ignored, featureContext) -> {
      val match = featureContext.getRelevantMatch();
      return match == null ? 0 : match.getCurrentTable().getRows().size();
    };
  }

  Feature currentColumnCount() {
    return (ignored, featureContext) -> {
      val match = featureContext.getRelevantMatch();
      return match == null ? 0 : match.getCurrentTable().getColumns().size();
    };
  }

  Feature currentCellCount() {
    return (ignored, featureContext) -> {
      val match = featureContext.getRelevantMatch();
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
    return (revision, featureContext) -> {
      if (revision.getParsed() == null) {
        return 1d;
      }

      final double removedTableCount = featureContext.getResult().getRemovedTables().size();
      final double currentTableCount = revision.getParsed().size();
      return (currentTableCount - removedTableCount) / currentTableCount;
    };
  }

  Feature unmatchedRows() {
    return (ignored, featureContext) -> {
      if (featureContext.getRowMatchResult() == null) {
        return 1d;
      }

      final double matchedRowCount = featureContext.getRowMatchResult().getMatches().size();
      final double totalRowCount = featureContext.getRelevantMatch().getCurrentTable().getRows().size();

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

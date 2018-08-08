package features.content;

import features.Feature;
import features.content.util.TableContentExtractor;
import features.content.util.table.EmptyCellChangeRatio;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
import features.content.util.table.SizePerCellChangeRatio;
import features.content.util.table.SyntaxChecker;
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

  Feature unmatchedTables() {
    return parameters -> {
      final double removedTableCount = parameters.getResult().getRemovedTables().size();
      final double currentTableCount = parameters.getResult().getMatches().size() + parameters.getResult().getAddedTables().size();
      if (currentTableCount == 0) {
        return 1;
      }
      return (currentTableCount - removedTableCount) / currentTableCount;
    };
  }

  Feature unmatchedRows() {
    return parameters -> {
      if (parameters.getRowMatchResult() == null) {
        return 1d;
      }

      final double matchedRowCount = parameters.getRowMatchResult().getMatches().size();
      double totalRowCount = 0;
      if (parameters.getRelevantMatch() != null) {
        totalRowCount = parameters.getRelevantMatch().getCurrentTable().getRows().size();
      }
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

  Feature tableClipRatio() {
    return parameters -> SyntaxChecker.tableClipRatio(TableContentExtractor.getContent(parameters));
  }

  Feature openAndCloseSyntaxRatio() {
    return parameters -> SyntaxChecker.openAndCloseSyntaxRatio(TableContentExtractor.getContent(parameters));
  }

  Feature sizePerCellChangeRatio() {
    return new SizePerCellChangeRatio();
  }

  Feature emptyCellRatio() {
    return new EmptyCellChangeRatio();
  }

}

package features.content;

import features.Feature;
import features.content.util.TableContentExtractor;
import features.content.util.table.FilledCellRatio;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
import features.content.util.table.SyntaxChecker;
import features.content.util.table.TableGeometry;
import features.content.util.table.TableGeometry.Measure;
import lombok.val;
import util.BasicUtils;

class TableFeatureFactory {

  Feature currentRowCount() {
    return parameters -> {
      int totalRowCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalRowCount += table.getCurrentTable().getRows().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalRowCount += table.getRows().size();
      }
      return totalRowCount;
    };
  }

  Feature currentColumnCount() {
    return parameters -> {
      int totalColumnCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalColumnCount += table.getCurrentTable().getColumns().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalColumnCount += table.getColumns().size();
      }
      return totalColumnCount;
    };
  }

  Feature currentCellCount() {
    return parameters -> {
      int totalCellCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalCellCount += table.getCurrentTable().getRows().size() * table.getCurrentTable().getColumns().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalCellCount += table.getRows().size() * table.getColumns().size();
      }
      return totalCellCount;
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
      int totalRowCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalRowCount += table.getCurrentTable().getRows().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalRowCount += table.getRows().size();
      }
      if (totalRowCount == 0) {
        return 1;
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

  Feature clipCount() {
    return parameters -> SyntaxChecker.checkClipCount(TableContentExtractor.getContent(parameters));
  }

  Feature refCount() {
    return parameters -> SyntaxChecker.checkRefCount(TableContentExtractor.getContent(parameters));
  }

  Feature sizePerTable() {
    return parameters -> {
      double size = BasicUtils.parsedLength(parameters.getRevision().getParsed());
      int tableCount = parameters.getResult().getMatches().size() + parameters.getResult().getAddedTables().size();
      if (tableCount == 0) {
        return -1;
      }
      return size / tableCount;
    };
  }

  Feature sizePerRow() {
    return parameters -> {
      double size = BasicUtils.parsedLength(parameters.getRevision().getParsed());

      int totalRowCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalRowCount += table.getCurrentTable().getRows().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalRowCount += table.getRows().size();
      }
      if (totalRowCount == 0) {
        return -1;
      }

      return size / totalRowCount;
    };
  }

  Feature sizePerColumn() {
    return parameters -> {
      double size = BasicUtils.parsedLength(parameters.getRevision().getParsed());

      int totalColumnCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalColumnCount += table.getCurrentTable().getColumns().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalColumnCount += table.getColumns().size();
      }
      if (totalColumnCount == 0) {
        return -1;
      }

      return size / totalColumnCount;
    };
  }

  Feature sizePerCell() {
    return parameters -> {
      double size = BasicUtils.parsedLength(parameters.getRevision().getParsed());

      int totalCellCount = 0;
      for (val table : parameters.getResult().getMatches()) {
        totalCellCount += table.getCurrentTable().getRows().size() * table.getCurrentTable().getColumns().size();
      }
      for (val table : parameters.getResult().getAddedTables()) {
        totalCellCount += table.getRows().size() * table.getColumns().size();
      }
      if (totalCellCount == 0) {
        return -1;
      }

      return size / totalCellCount;
    };
  }

  Feature emptyCellRatio() {
    return new FilledCellRatio();
  }

}

package features.content;

import features.Feature;
import features.content.util.TableContentExtractor;
import features.content.util.table.EmptyCellChange;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
import features.content.util.table.SizePerCellChange;
import features.content.util.table.SyntaxChecker;
import features.content.util.table.TableGeometry;
import features.content.util.table.TableGeometry.Measure;
import features.content.util.typing.DataTypeDependentFeatureFactory;
import lombok.experimental.Delegate;
import lombok.val;

class TableFeatureFactory {

  @Delegate
  private final DataTypeDependentFeatureFactory withTypes = new DataTypeDependentFeatureFactory();

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

  Feature tableClipCount() {
    return parameters -> SyntaxChecker.getTableClipCount(TableContentExtractor.getContent(parameters));
  }

  Feature openAndCloseSyntaxCount() {
    return parameters -> SyntaxChecker.getOpenAndCloseSyntaxCount(TableContentExtractor.getContent(parameters));
  }

  Feature sizePerCellChangeRatio() {
    return SizePerCellChange::getRatio;
  }

  Feature sizePerCell() {
    return SizePerCellChange::getSizePerCell;
  }

  Feature emptyCellRatio() {
    return EmptyCellChange::getRatio;
  }

  Feature emptyCellCount() {
    return EmptyCellChange::getCount;
  }

}

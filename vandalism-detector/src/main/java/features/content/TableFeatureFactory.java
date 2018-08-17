package features.content;

import features.Feature;
import features.content.util.table.EmptyCellChange;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
import features.content.util.table.SizePerCellChange;
import features.content.util.table.SyntaxChecker;
import features.content.util.table.TableGeometry;
import features.content.util.table.TableGeometry.Measure;
import features.content.util.typing.DataTypeDependentFeatureFactory;
import features.content.wikisyntax.AddedInvalidAttributes;
import lombok.experimental.Delegate;
import lombok.val;
import util.BasicUtils;

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

  Feature unmatchedTableRatio() {
    return parameters -> {
      // Of X previous existing tables, Y % were removed
      final int previousTableCount = BasicUtils.getPreviousTables(parameters).size();
      final int removedTables = parameters.getResult().getRemovedTables().size();
      if (removedTables == 0) {
        return 0;
      }
      return (double) removedTables / previousTableCount;
    };
  }

  Feature unmatchedRowRatio() {
    return parameters -> {
      // Or X previous existing rows, Y % were removed
      val result = parameters.getRowMatchResult();
      if (result == null) {
        // There has been no relevant match and thus no rows to match.
        return 0;
      }

      final int previousRows = result.getMatches().size() + result.getDeletedRows().size();
      final int removedRows = result.getDeletedRows().size();
      if (removedRows == 0) {
        return 0;
      }
      return (double) removedRows / previousRows;
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

  Feature openAndCloseSyntaxCount() {
    return parameters -> SyntaxChecker.getOpenAndCloseSyntaxCount(parameters.getContent());
  }

  Feature sizePerCellChangeRatio() {
    return SizePerCellChange::getRatio;
  }

  Feature sizePerCell() {
    return SizePerCellChange::getSizePerCell;
  }

  Feature addedEmptyCellRatio() {
    return EmptyCellChange::addedRatio;
  }

  Feature removedEmptyCellRatio() {
    return EmptyCellChange::removeRatio;
  }

  Feature emptyCellCount() {
    return EmptyCellChange::getCount;
  }

  Feature addedInvalidAttributes() {
    return new AddedInvalidAttributes();
  }

}

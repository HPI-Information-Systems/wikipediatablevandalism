package features.content;

import features.Feature;
import features.content.util.table.AverageCellSizeChange;
import features.content.util.table.EmptyCellChange;
import features.content.util.table.RankChange;
import features.content.util.table.SharedCellRatio;
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

  Feature averageCellSizeIncrease() {
    return AverageCellSizeChange::averageCellSizeIncrease;
  }

  Feature averageCellSizeDecrease() {
    return AverageCellSizeChange::averageCellSizeDecrease;
  }

  Feature averageCellSize() {
    return AverageCellSizeChange::averageCellSize;
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

  Feature isTableAdded() {
    return parameters -> parameters.getResult().getAddedTables().isEmpty() ? 0 : 1;
  }

  Feature addedTableCount() {
    return parameters -> parameters.getResult().getAddedTables().size();
  }

  Feature isTableModified() {
    return parameters -> {
      for (val match : parameters.getResult().getMatches()) {
        boolean identical = match.getPreviousTable().equals(match.getCurrentTable());
        if (!identical) {
          return 1;
        }
      }
      return 0;
    };
  }

  Feature modifiedTableCount() {
    return parameters -> {
      int modified = 0;
      for (val match : parameters.getResult().getMatches()) {
        boolean identical = match.getPreviousTable().equals(match.getCurrentTable());
        if (!identical) {
          ++modified;
        }
      }
      return modified;
    };
  }

  Feature isTableDeleted() {
    return parameters -> parameters.getResult().getRemovedTables().isEmpty() ? 0 : 1;
  }

  Feature deletedTableCount() {
    return parameters -> parameters.getResult().getRemovedTables().size();
  }

  Feature areMultipleTablesChanged() {
    return parameters -> {
      int modified = 0;
      for (val match : parameters.getResult().getMatches()) {
        val identical = match.getPreviousTable().equals(match.getCurrentTable());
        if (!identical) {
          ++modified;
        }

        if (modified == 2) {
          return 1;
        }
      }
      return 0;
    };
  }

  Feature isTableReplacement() {
    return parameters -> {
      int removed = parameters.getResult().getRemovedTables().size();
      int added = parameters.getResult().getAddedTables().size();
      if (removed == added) {
        return 1;
      }
      return 0;
    };
  }
}

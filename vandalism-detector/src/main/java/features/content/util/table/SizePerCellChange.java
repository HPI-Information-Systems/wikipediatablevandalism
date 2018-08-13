package features.content.util.table;

import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class SizePerCellChange {

  static public double getRatio(FeatureParameters parameters) {
    double currentSizePerCell = 0;
    double previousSizePerCell = 0;
    if (parameters.getRelevantMatch() != null) {
      currentSizePerCell = getSizePerCell(parameters.getRelevantMatch().getCurrentTable(), parameters.getRevision());
      if (parameters.getPreviousRevision() != null) {
        previousSizePerCell = getSizePerCell(parameters.getRelevantMatch().getPreviousTable(), parameters.getPreviousRevision());
      }
    }
    if (previousSizePerCell == 0) {
      if (currentSizePerCell == 0) {
        return 0;
      }
      return 1;
    }
    return (currentSizePerCell - previousSizePerCell) / previousSizePerCell;
  }

  static public double getSizePerCell(FeatureParameters parameters) {
    if (parameters.getRelevantMatch() != null) {
      return getSizePerCell(parameters.getRelevantMatch().getCurrentTable(),
          parameters.getRevision());
    }
    return 0;
  }

  static private double getSizePerCell(WikiTable table, MyRevisionType revision) {
    double size = BasicUtils.parsedLength(revision.getParsed());
    double totalCellCount = table.getRows().size() * table.getColumns().size();
    if (totalCellCount == 0) {
      return size;
    }
    return size / totalCellCount;
  }

}

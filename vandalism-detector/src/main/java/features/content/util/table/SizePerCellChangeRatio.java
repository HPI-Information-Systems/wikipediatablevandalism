package features.content.util.table;

import features.Feature;
import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class SizePerCellChangeRatio implements Feature {

  @Override
  public double getValue(FeatureParameters parameters) {
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

  private double getSizePerCell(WikiTable table, MyRevisionType revision) {
    double size = BasicUtils.parsedLength(revision.getParsed());
    double totalCellCount = table.getRows().size() * table.getColumns().size();
    if (totalCellCount == 0) {
      return size;
    }
    return size / totalCellCount;
  }

}

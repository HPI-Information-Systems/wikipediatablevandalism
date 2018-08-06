package features.content.util.table;

import features.Feature;
import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class SizePerCellChangeRatio implements Feature {

  @Override
  public double getValue(FeatureParameters parameters) {
    double currentSizePerCellRatio = 0;
    double previousSizePerCellRatio = 0;
    if (parameters.getRelevantMatch() != null) {
      currentSizePerCellRatio = getSizePerCellRatio(parameters.getRelevantMatch().getCurrentTable(), parameters.getRevision());
      if (parameters.getPreviousRevision() != null) {
        previousSizePerCellRatio = getSizePerCellRatio(parameters.getRelevantMatch().getPreviousTable(), parameters.getPreviousRevision());
      }
    }
    if (previousSizePerCellRatio == 0) {
      if (currentSizePerCellRatio == 0) {
        return 0;
      }
      return 1;
    }
    return (currentSizePerCellRatio - previousSizePerCellRatio) / previousSizePerCellRatio;
  }

  private double getSizePerCellRatio(WikiTable table, MyRevisionType revision) {
    double size = BasicUtils.parsedLength(revision.getParsed());
    double totalCellCount = table.getRows().size() * table.getColumns().size();
    if (totalCellCount == 0) {
      return size;
    }
    return size / totalCellCount;
  }

}

package features.content;

import features.Feature;
import features.content.TableGeometry.Measure;
import lombok.RequiredArgsConstructor;
import matching.row.RowMatchService;

@RequiredArgsConstructor
class ContentFeatureFactory {

  private final RowMatchService rowMatchService;

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
    return new RankChange(rowMatchService);
  }

  Feature ratioOfNumericalCharsToAllChars() {
    return (revision, ignored) -> {
      String  tableContents = Utils.getTableContents(revision.getParsed());
      if (tableContents.length() == 0) {
        return 0;
      }
      float numericalCount = 0;
      for (char c : tableContents.toCharArray()) {
        if (!Character.isDigit(c)) {
          ++numericalCount;
        }
      }
      return numericalCount / (float) tableContents.length();
    };
  }

}

package features.content;

import features.Feature;
import features.content.TableGeometry.Measure;
import lombok.RequiredArgsConstructor;
import matching.row.RowMatchService;

@RequiredArgsConstructor
public class ContentFeatureFactory {

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

}

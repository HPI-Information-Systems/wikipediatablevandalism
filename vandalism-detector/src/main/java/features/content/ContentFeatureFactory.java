package features.content;

import features.Feature;
import features.content.TableGeometry.Measure;
import lombok.RequiredArgsConstructor;
import matching.table.TableMatchService;
import matching.row.RowMatchService;

@RequiredArgsConstructor
public class ContentFeatureFactory {

  private final TableMatchService matchService;
  private final RowMatchService rowMatchService;

  Feature cellCount() {
    return new TableGeometry(matchService, Measure.Product);
  }

  Feature columnCount() {
    return new TableGeometry(matchService, Measure.Columns);
  }

  Feature rowCount() {
    return new TableGeometry(matchService, Measure.Rows);
  }

  Feature sharedCellRatio() {
    return new SharedCellRatio(matchService);
  }

  Feature rankChange() {
    return new RankChange(matchService, rowMatchService);
  }

}

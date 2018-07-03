package features.content;

import features.Feature;
import features.content.TableGeometry.Measure;
import lombok.RequiredArgsConstructor;
import matching.MatchService;

@RequiredArgsConstructor
public class ContentFeatureFactory {

  private final MatchService matchService;

  Feature cellCount() {
    return new TableGeometry(matchService, Measure.Product);
  }

  Feature columnCount() {
    return new TableGeometry(matchService, Measure.Columns);
  }

  Feature rowCount() {
    return new TableGeometry(matchService, Measure.Rows);
  }

}

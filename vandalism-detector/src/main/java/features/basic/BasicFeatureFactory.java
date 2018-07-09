package features.basic;

import features.Feature;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
class BasicFeatureFactory {

  Feature rowCount() {
    return (revision, featureContext) -> {
      val match = featureContext.getRelevantMatch();
      return match == null ? -1 : match.getCurrentTable().getRows().size();
    };
  }

  Feature columnCount() {
    return (revision, featureContext) -> {
      val match = featureContext.getRelevantMatch();
      return match == null ? -1 : match.getCurrentTable().getColumns().size();
    };
  }

  Feature cellCount() {
    return (revision, featureContext) -> {
      val match = featureContext.getRelevantMatch();
      if (match == null) {
        return -1;
      }

      val rows = match.getCurrentTable().getRows().size();
      val columns = match.getCurrentTable().getColumns().size();
      return rows * columns;
    };
  }
}

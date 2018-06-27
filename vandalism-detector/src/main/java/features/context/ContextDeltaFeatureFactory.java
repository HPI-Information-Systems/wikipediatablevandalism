package features.context;

import features.DeltaFeature;
import java.time.temporal.ChronoUnit;

class ContextDeltaFeatureFactory {

  DeltaFeature hoursSinceLastEdit() {
    return (precursors, revision) -> new TimeSinceLastArticle(ChronoUnit.HOURS);
  }



}

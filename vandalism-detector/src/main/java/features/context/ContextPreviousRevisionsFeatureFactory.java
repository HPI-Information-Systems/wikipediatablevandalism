package features.context;

import features.Feature;
import java.time.temporal.ChronoUnit;

public class ContextPreviousRevisionsFeatureFactory {

  Feature timeSinceLastArticleEditBySameContributor() {
    return new TimeSinceLastArticleEdit(ChronoUnit.MINUTES); // TODO maybe seconds
  }

}

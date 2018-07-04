package features.context;

import features.Feature;

public class ContextPreviousRevisionsFeatureFactory {

  Feature timeSinceLastArticleEditBySameContributor() {
    return (revision, featureContext) -> new TimeSinceLastArticleEdit()
        .getValue(revision, featureContext);
  }

}

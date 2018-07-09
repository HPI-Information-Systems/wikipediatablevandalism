package features.future;

import features.Feature;

class FutureFeatureFactory {

  Feature isCommentDeleted() {
    return (revision, featureContext) -> revision.getComment() != null
        && revision.getComment().getDeleted() != null;
  }

  Feature isContributorDeleted() {
    return (revision, featureContext) -> revision.getContributor().getDeleted() != null;
  }
}

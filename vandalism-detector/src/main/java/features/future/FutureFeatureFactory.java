package features.future;

import features.Feature;

class FutureFeatureFactory {

  Feature isCommentDeleted() {
    return (revision, context) -> revision.getComment() != null
        && revision.getComment().getDeleted() != null;
  }

  Feature isContributorDeleted() {
    return (revision, context) -> revision.getContributor().getDeleted() != null;
  }
}

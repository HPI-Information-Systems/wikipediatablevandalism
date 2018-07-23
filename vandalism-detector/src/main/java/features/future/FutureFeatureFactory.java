package features.future;

import features.Feature;
import lombok.val;

class FutureFeatureFactory {

  Feature isCommentDeleted() {
    return parameters -> {
      val comment = parameters.getRevision().getComment();
      return comment != null && comment.getDeleted() != null;
    };
  }

  Feature isContributorDeleted() {
    return parameters -> parameters.getRevision().getContributor().getDeleted() != null;
  }
}

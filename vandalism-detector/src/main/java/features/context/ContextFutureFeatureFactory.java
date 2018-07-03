package features.context;

import features.Feature;

/**
 * Create all features connected to the future or next revisions.
 */
public class ContextFutureFeatureFactory {

  Feature isContributorDeleted() {
    return (revision, ignored) -> revision.getContributor().getDeleted() != null;
  }

  Feature isCommentDeleted() {
    return (revision, ignored) -> revision.getComment() != null
        && revision.getComment().getDeleted() != null;
  }

}

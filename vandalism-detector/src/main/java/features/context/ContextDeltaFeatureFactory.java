package features.context;

import features.Feature;
import lombok.val;

/**
 * Create all features connected to the context of the previous and the actual revision.
 */
class ContextDeltaFeatureFactory {

  Feature timeSinceLastArticleEdit() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision != null) {
        val revisionTotalTime = Utils.totalTime(revision.getTimestamp());
        val previousRevisionTotalTime = Utils.totalTime(previousRevision.getTimestamp());
        return revisionTotalTime - previousRevisionTotalTime;
      }
      return -1;
    };
  }

  Feature isPreviousSameContributor() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision != null) {
        val contributor = revision.getContributor();
        val previousContributor = previousRevision.getContributor();
        if (!Utils.isAnonymous(contributor) && !Utils.isAnonymous(previousContributor)) {
          return contributor.getId().equals(previousContributor.getId());
        } else if (Utils.isAnonymous(contributor) && Utils.isAnonymous(previousContributor)) {
          return contributor.getIp().equals(previousContributor.getIp());
        } else {
          return false;
        }
      }
      return false;
    };
  }

  Feature sizeRatio() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision != null) {
        val revisionParsedLength = Utils.parsedLength(revision.getParsed());
        val previousRevisionParsedLength = Utils.parsedLength(previousRevision.getParsed());
        return (float) revisionParsedLength / (float) previousRevisionParsedLength;
      }
      return 0;
    };
  }

  Feature sizeChange() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision != null) {
        val revisionParsedLength = Utils.parsedLength(revision.getParsed());
        val previousRevisionParsedLength = Utils.parsedLength(previousRevision.getParsed());
        return revisionParsedLength - previousRevisionParsedLength;
      }
      return 0;
    };
  }

}

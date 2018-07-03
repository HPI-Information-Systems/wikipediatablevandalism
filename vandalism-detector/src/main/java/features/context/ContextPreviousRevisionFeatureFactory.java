package features.context;

import features.Feature;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.val;

/**
 * Create all features connected to the context of the previous and the actual revision.
 */
class ContextPreviousRevisionFeatureFactory {

  Feature timeSinceLastArticleEdit() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision != null) {
        val revisionTime = revision.getDate().toInstant();
        val previousRevisionTime = previousRevision.getDate().toInstant();
        return Duration.between(previousRevisionTime, revisionTime)
            .get(ChronoUnit.MINUTES); // TODO maybe seconds
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
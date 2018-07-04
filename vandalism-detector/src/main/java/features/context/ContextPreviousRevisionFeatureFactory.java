package features.context;

import com.google.common.base.Preconditions;
import features.Feature;
import java.time.Duration;
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
        Preconditions.checkState(previousRevisionTime.isBefore(revisionTime),
            "previousRevisionTime should be before revisionTime");
        return Duration.between(previousRevisionTime, revisionTime)
            .toMinutes(); // TODO maybe use getSeconds() instead
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

  Feature sizeChange() { // better than sizeRation -> what is when revisionParsedLength = 0
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

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
      if (previousRevision == null) {
        return -1;
      }
      val revisionTime = revision.getDate().toInstant();
      val previousRevisionTime = previousRevision.getDate().toInstant();
      Preconditions.checkState(previousRevisionTime.isBefore(revisionTime),
          "previousRevisionTime should be before revisionTime");
      return Duration.between(previousRevisionTime, revisionTime)
          .toMinutes(); // TODO maybe use getSeconds() instead
    };
  }

  Feature isPreviousSameContributor() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision == null) {
        return false;
      }
      return Utils.hasSameContributor(revision, previousRevision);
    };
  }

  Feature sizeRatio() {
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      if (previousRevision == null) {
        return 0;
      }
      val revisionParsedLength = Utils.parsedLength(revision.getParsed());
      val previousRevisionParsedLength = Utils.parsedLength(previousRevision.getParsed());
      return (float) revisionParsedLength / (float) previousRevisionParsedLength;
    };
  }

  Feature sizeChange() { // better than sizeRation -> what is when revisionParsedLength = 0 TODO better parsing? -> put in ContentFeatures?
    return (revision, featureContext) -> {
      val previousRevision = Utils.getPreviousRevision(featureContext.getPreviousRevisions());
      int previousRevisionParsedLength = 0;
      if (previousRevision != null) {
        previousRevisionParsedLength = Utils.parsedLength(previousRevision.getParsed());
      }
      return Utils.parsedLength(revision.getParsed()) - previousRevisionParsedLength;
    };
  }

}

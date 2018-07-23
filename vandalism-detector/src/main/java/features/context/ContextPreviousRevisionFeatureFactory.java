package features.context;

import com.google.common.base.Preconditions;
import features.Feature;
import java.time.Duration;
import lombok.val;
import util.BasicUtils;

/**
 * Create all features connected to the context of the previous and the actual revision.
 */
class ContextPreviousRevisionFeatureFactory {

  Feature timeSinceLastArticleEdit() {
    return parameters -> {
      val revision = parameters.getRevision();
      val previousRevision = parameters.getPreviousRevision();
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

  Feature hasPreviousSameContributor() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      if (previousRevision == null) {
        return false;
      }
      return BasicUtils.hasSameContributor(parameters.getRevision(), previousRevision);
    };
  }

  Feature sizeChange() { //TODO better parsing? -> put in ContentFeatures?
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      int previousRevisionParsedLength = 0;
      if (previousRevision != null) {
        previousRevisionParsedLength = BasicUtils.parsedLength(previousRevision.getParsed());
      }
      return BasicUtils.parsedLength(parameters.getRevision().getParsed())
          - previousRevisionParsedLength;
    };
  }

}

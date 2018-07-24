package features.context;

import com.google.common.collect.Lists;
import features.Feature;
import features.context.util.ContributorRevertedBeforeInThatArticleUtil;
import features.context.util.TimeSinceFirstRevisionBySameContributorUtil;
import lombok.val;
import util.BasicUtils;

/**
 * Create all features based on the metadata connected to the whole page (future and past)
 */
class PageFeatureFactory {

  Feature timeSinceLastArticleEdit() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      if (previousRevision == null) {
        return -1;
      }
      return BasicUtils.getTimeDuration(parameters.getRevision(), previousRevision);
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

  Feature timeSinceLastArticleEditBySameContributor() {
    return parameters -> TimeSinceFirstRevisionBySameContributorUtil.getTime(parameters.getRevision(), parameters.getPreviousRevisions());
  }

  Feature timeSinceFirstArticleEditBySameContributor() {
    return parameters -> TimeSinceFirstRevisionBySameContributorUtil.getTime(parameters.getRevision(), Lists.reverse(parameters.getPreviousRevisions()));
  }

  Feature revertCount() {
    return parameters -> {
      int revertCount = 0;
      for (val previousRevision : parameters.getPreviousRevisions()) {
        if (parameters.getRevision().getSha1().equals(previousRevision.getSha1())) {
          ++revertCount;
        }
      }
      return revertCount;
    };
  }

  Feature ratioOffAllEditsToContributorEdits() {
    return parameters -> {
      val previousRevisions = parameters.getPreviousRevisions();
      if (previousRevisions == null) {
        return -1;
      }
      double sameContributor = 0;
      for (val previousRevision : previousRevisions) {
        if (BasicUtils.hasSameContributor(parameters.getRevision(), previousRevision)) {
          ++sameContributor;
        }
      }
      return sameContributor / parameters.getPreviousRevisions().size();
    };
  }

  Feature contributorRevertedBeforeInThatArticleCount() {
    return ContributorRevertedBeforeInThatArticleUtil::getRevertedCount;
  }

  Feature timeSinceContributorRevertedBeforeInThatArticle() {
    return ContributorRevertedBeforeInThatArticleUtil::getTimeSinceLastReverted;
  }

}

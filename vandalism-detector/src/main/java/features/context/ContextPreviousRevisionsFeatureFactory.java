package features.context;

import features.Feature;
import lombok.val;

class ContextPreviousRevisionsFeatureFactory {

  Feature timeSinceLastArticleEditBySameContributor() {
    return (revision, featureContext) -> new TimeSinceLastArticleEdit()
        .getValue(revision, featureContext);
  }

  Feature isRevert() {
    return (revision, featureContext) -> {
      for (val previousRevision : featureContext.getPreviousRevisions()) {
        if (revision.getSha1().equals(previousRevision.getSha1())) {
          return true;
        }
      }
      return false;
    };
  }

  Feature ratioOffAllEditsToContributorEdits() {
    return (revision, featureContext) -> {
      float sameContributor = 1;
      for (val previousRevision : featureContext.getPreviousRevisions()) {
        if (Utils.isAnonymous(revision.getContributor()) &&
            Utils.isAnonymous(previousRevision.getContributor()) &&
            revision.getContributor().getIp().equals(previousRevision.getContributor().getIp())) {
          ++sameContributor;
        } else if (!Utils.isAnonymous(revision.getContributor()) &&
            !Utils.isAnonymous(previousRevision.getContributor()) &&
            revision.getContributor().getId().equals(previousRevision.getContributor().getId())) {
          ++sameContributor;
        }
      }
      return sameContributor / (float) (featureContext.getPreviousRevisions().size()+1);
    };
  }

  Feature wasContributorRevertedBeforeInThatArticle() {
    return (revision, featureContext) -> {
      return null;
    };
  }

}

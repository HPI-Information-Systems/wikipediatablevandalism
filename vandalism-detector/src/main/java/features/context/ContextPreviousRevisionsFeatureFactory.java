package features.context;

import features.Feature;
import java.util.ArrayList;
import java.util.List;
import lombok.val;

class ContextPreviousRevisionsFeatureFactory {

  Feature timeSinceLastArticleEditBySameContributor() {
    return (revision, featureContext) -> new TimeSinceLastArticleEdit()
        .getValue(revision, featureContext);
  }

  Feature revertCount() {
    return (revision, featureContext) -> {
      int revertCount = 0;
      for (val previousRevision : featureContext.getPreviousRevisions()) {
        if (revision.getSha1().equals(previousRevision.getSha1())) {
          ++revertCount;
        }
      }
      return revertCount;
    };
  }

  Feature ratioOffAllEditsToContributorEdits() {
    return (revision, featureContext) -> {
      float sameContributor = 1;
      for (val previousRevision : featureContext.getPreviousRevisions()) {
        if (Utils.hasSameContributor(revision, previousRevision)) {
          ++sameContributor;
        }
      }
      return sameContributor / (float) (featureContext.getPreviousRevisions().size() + 1);
    };
  }

  Feature wasContributorRevertedBeforeInThatArticle() {
    return (revision, featureContext) -> {
      int revertedRevisionCount = 0;
      List<String> searchedBefore = new ArrayList<>();
      val allRevisions = featureContext.getPreviousRevisions();
      allRevisions.add(0, revision);
      for (val currentRevision : allRevisions) {
        if (searchedBefore
            .contains(currentRevision.getSha1())) { // not search for already searched sha1 value
          continue;
        }
        searchedBefore.add(currentRevision.getSha1()); // save searched sha1 value

        // calculate reverted revisions indexes
        List<Integer> revertedRevisionsIndexes = new ArrayList<>();
        for (int i = 0; i < allRevisions.size(); ++i) {
          if (allRevisions.get(i).getSha1().equals(currentRevision.getSha1())) {
            revertedRevisionsIndexes.add(i);
          }
        }

        if (revertedRevisionsIndexes.size() > 1) { // find always currentRevision
          for (int i = 0; i < revertedRevisionsIndexes.size() - 1; ++i) {
            // search for contributor between i and i+1 ; start at 'get(i)+1' because you want to search just between
            for (int j = revertedRevisionsIndexes.get(i) + 1;
                j < revertedRevisionsIndexes.get(i + 1); ++j) {
              if (Utils.hasSameContributor(allRevisions.get(j), revision)) {
                ++revertedRevisionCount;
              }
            }
          }
        }
      }
      return revertedRevisionCount;
    };
  }

}

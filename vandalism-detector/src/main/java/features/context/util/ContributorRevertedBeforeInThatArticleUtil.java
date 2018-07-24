package features.context.util;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;

public class ContributorRevertedBeforeInThatArticleUtil {

  public static long getRevertedCount(final FeatureParameters parameters) {
    long revertedRevisionCount = 0;
    List<String> searchedBefore = new ArrayList<>();
    val allRevisions = createAllRevisions(parameters);

    for (val currentRevision : allRevisions) {
      if (searchedBefore
          .contains(currentRevision.getSha1())) { // not search for already searched sha1 value
        continue;
      }
      searchedBefore.add(currentRevision.getSha1()); // save searched sha1 value

      List<Integer> revertedRevisionsIndexes = calculateRevertedRevisionsIndexes(allRevisions,
          currentRevision);
      revertedRevisionCount += calculateRevertedRevisions(revertedRevisionsIndexes,
          allRevisions, parameters.getRevision(), true);
    }

    return revertedRevisionCount;
  }

  public static long getTimeSinceLastReverted(final FeatureParameters parameters) {
    List<String> searchedBefore = new ArrayList<>();
    val allRevisions = createAllRevisions(parameters);

    for (val currentRevision : allRevisions) {
      if (searchedBefore
          .contains(currentRevision.getSha1())) { // not search for already searched sha1 value
        continue;
      }
      searchedBefore.add(currentRevision.getSha1()); // save searched sha1 value

      List<Integer> revertedRevisionsIndexes = calculateRevertedRevisionsIndexes(allRevisions,
          currentRevision);
      long timeSinceContributorLastRevertedInThatArticle = calculateRevertedRevisions(
          revertedRevisionsIndexes, allRevisions, parameters.getRevision(), false);
      if (timeSinceContributorLastRevertedInThatArticle != -1) {
        return timeSinceContributorLastRevertedInThatArticle;
      }
    }

    return -1;
  }

  private static List<MyRevisionType> createAllRevisions(final FeatureParameters parameters) {
    val allRevisions = parameters.getPreviousRevisions();
    allRevisions.add(0, parameters.getRevision());
    return allRevisions;
  }

  // calculate reverted revisions indexes
  private static List<Integer> calculateRevertedRevisionsIndexes(
      final List<MyRevisionType> allRevisions, final MyRevisionType currentRevision) {
    List<Integer> revertedRevisionsIndexes = new ArrayList<>();
    for (int i = 0; i < allRevisions.size(); ++i) {
      if (allRevisions.get(i).getSha1().equals(currentRevision.getSha1())) {
        revertedRevisionsIndexes.add(i);
      }
    }
    return revertedRevisionsIndexes;
  }

  private static long calculateRevertedRevisions(final List<Integer> revertedRevisionsIndexes,
      final List<MyRevisionType> allRevisions, final MyRevisionType revision, final boolean calculateCount) {
    long revertedRevisionCount = 0;
    if (revertedRevisionsIndexes.size() > 1) { // find always currentRevision
      for (int i = 0; i < revertedRevisionsIndexes.size() - 1; ++i) {
        // search for contributor between i and i+1 ; start at 'get(i)+1' because you want to search just between
        for (int j = revertedRevisionsIndexes.get(i) + 1;
            j < revertedRevisionsIndexes.get(i + 1); ++j) {
          if (BasicUtils.hasSameContributor(allRevisions.get(j), revision)) {
            if (calculateCount) {
              ++revertedRevisionCount;
            } else {
              val revisionTime = revision.getDate().toInstant();
              val previousRevisionTime = allRevisions.get(j).getDate().toInstant();
              Preconditions.checkState(previousRevisionTime.isBefore(revisionTime),
                  "previousRevisionTime should be before revisionTime");
              return Duration.between(previousRevisionTime, revisionTime)
                  .toMinutes();
            }
          }
        }
      }
    }

    if (calculateCount) {
      return revertedRevisionCount;
    } else {
      return -1;
    }
  }

}

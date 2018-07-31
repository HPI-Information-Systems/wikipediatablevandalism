package features.context.util;

import java.util.ArrayList;
import java.util.List;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;

public class ContributorRevertedBeforeInThatArticle {

  public static double getRevertedCount(final FeatureParameters parameters) {
    int revertedRevisionCount = 0;
    List<String> searchedBefore = new ArrayList<>();
    val allRevisions = createAllRevisions(parameters);

    for (val currentRevision : allRevisions) {
      // not search for already searched sha1 value
      if (searchedBefore.contains(currentRevision.getSha1())) {
        continue;
      }
      // save searched sha1 value
      searchedBefore.add(currentRevision.getSha1());

      List<Integer> revertedRevisionsIndexes = calculateRevertedRevisionsIndexes(allRevisions,
          currentRevision);
      val revertedRevisionsBySameContributor = calculateRevertedRevisionsBySameContributor(revertedRevisionsIndexes,
          allRevisions,
          parameters.getRevision());
      revertedRevisionCount += revertedRevisionsBySameContributor.size();
    }

    return revertedRevisionCount;
  }

  public static double getTimeSinceLastReverted(final FeatureParameters parameters) {
    List<String> searchedBefore = new ArrayList<>();
    val allRevisions = createAllRevisions(parameters);

    for (val currentRevision : allRevisions) {
      // not search for already searched sha1 value
      if (searchedBefore.contains(currentRevision.getSha1())) {
        continue;
      }
      // save searched sha1 value
      searchedBefore.add(currentRevision.getSha1());

      List<Integer> revertedRevisionsIndexes = calculateRevertedRevisionsIndexes(allRevisions,
          currentRevision);
      val revertedRevisionsBySameContributor = calculateRevertedRevisionsBySameContributor(revertedRevisionsIndexes,
          allRevisions,
          parameters.getRevision());
      if (!revertedRevisionsBySameContributor.isEmpty()) {
        return BasicUtils.getTimeDuration(parameters.getRevision(), revertedRevisionsBySameContributor.get(0));
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

  private static List<MyRevisionType> calculateRevertedRevisionsBySameContributor(
      final List<Integer> revertedRevisionsIndexes,
      final List<MyRevisionType> allRevisions, final MyRevisionType revision) {
    List<MyRevisionType> revertedRevisions = new ArrayList<>();
    if (revertedRevisionsIndexes.size() > 1) { // find always currentRevision
      for (int i = 0; i < revertedRevisionsIndexes.size() - 1; ++i) {
        // search for contributor between i and i+1 ; start at 'get(i)+1' because you want to search just between
        for (int j = revertedRevisionsIndexes.get(i) + 1;
            j < revertedRevisionsIndexes.get(i + 1); ++j) {
          if (BasicUtils.hasSameContributor(allRevisions.get(j), revision)) {
            revertedRevisions.add(allRevisions.get(j));
          }
        }
      }
    }
    return revertedRevisions;
  }

}

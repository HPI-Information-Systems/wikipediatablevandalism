package features.context.impl;

import static java.util.stream.Collectors.toList;

import features.Feature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;

public class PageOwnership implements Feature {

  @Override
  public double getValue(FeatureParameters parameters) {
    final ContributorType currentContributor = parameters.getRevision().getContributor();

    if (BasicUtils.isAnonymous(currentContributor)) {
      return -1;
    }

    final Map<String, Integer> contributionCounts = contributionCounts(parameters);
    final int myContributions = contributionCounts
        .getOrDefault(currentContributor.getUsername(), 0);

    if (myContributions == 0) {
      return 0;
    }

    final List<Integer> ranks = contributionCounts.values()
        .stream()
        .distinct()
        .sorted()
        .collect(toList());

    return (float) ranks.indexOf(myContributions) / ranks.size();
  }

  private Map<String, Integer> contributionCounts(final FeatureParameters parameters) {
    final Map<String, Integer> editCounts = new HashMap<>();

    for (final MyRevisionType revision : parameters.getPreviousRevisions()) {

      if (BasicUtils.isAnonymous(revision.getContributor())) {
        continue;
      }

      final String username = revision.getContributor().getUsername();
      editCounts.compute(username, (name, counter) -> {
        if (counter == null) {
          return 1;
        }

        return counter + 1;
      });
    }

    return editCounts;
  }
}

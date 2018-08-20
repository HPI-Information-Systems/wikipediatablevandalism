package features.context.impl;

import features.Feature;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Number of distinct authors on a given revision set. Intuition: model feasibility of edit war. One
 * author unlikely (not going to argue with himself), two authors means feasible, with more than two
 * authors its again unlikely.
 */
@RequiredArgsConstructor
public class AuthorDiversity implements Feature {

  private final RevisionProvider revisionProvider;

  @Override
  public double getValue(FeatureParameters parameters) {
    final Set<String> authors = collectAuthors(revisionProvider.get(parameters));
    return authors.size();
  }

  private Set<String> collectAuthors(final List<MyRevisionType> revisions) {
    final Set<String> authors = new HashSet<>();
    for (final MyRevisionType revision : revisions) {

      if (BasicUtils.isAnonymous(revision.getContributor())) {
        continue;
      }

      authors.add(revision.getContributor().getUsername());
    }
    return authors;
  }
}

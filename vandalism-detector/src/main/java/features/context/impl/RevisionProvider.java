package features.context.impl;

import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import model.FeatureParameters;
import util.predicates.OlderThan;
import wikixmlsplit.datastructures.MyRevisionType;

@FunctionalInterface
public interface RevisionProvider {

  List<MyRevisionType> get(FeatureParameters parameters);

  static RevisionProvider all() {
    return FeatureParameters::getPreviousRevisions;
  }

  static RevisionProvider lastN(final int limit) {
    return parameters -> {
      val revisions = parameters.getPreviousRevisions();
      return revisions.subList(0, Math.min(limit, revisions.size()));
    };
  }

  static RevisionProvider maxAge(final TemporalAmount maxAge) {
    return parameters -> {
      final List<MyRevisionType> revisions = new ArrayList<>(parameters.getPreviousRevisions());
      revisions.removeIf(OlderThan.ofRevisionAndMaxAge(parameters.getRevision(), maxAge));
      return revisions;
    };
  }

  static RevisionProvider lastNWithMaxAge(final int limit, final TemporalAmount maxAge) {
    return parameters -> {
      final List<MyRevisionType> previous = parameters.getPreviousRevisions();
      final List<MyRevisionType> revisions = new ArrayList<>(limit);
      revisions.addAll(previous.subList(0, Math.min(limit, previous.size())));
      revisions.removeIf(OlderThan.ofRevisionAndMaxAge(parameters.getRevision(), maxAge));
      return revisions;
    };
  }
}

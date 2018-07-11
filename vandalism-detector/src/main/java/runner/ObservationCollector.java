package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.RevisionTag;
import parser.RevisionTagParser;

@RequiredArgsConstructor
public class ObservationCollector {

  private final Arguments arguments;

  public List<RevisionTag> collectObservations() {
    try {
      val parser = new RevisionTagParser();
      final List<RevisionTag> observations = parser.load(arguments.getRevisionTagPath());
      return filter(observations);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<RevisionTag> filter(final List<RevisionTag> observations) {
    // No filter by tag - limit to N observations
    if (arguments.getTagId() == Arguments.INVALID_TAG_ID) {
      limitObservations(observations);
      return observations;
    }

    // Partition observations into two buckets: (not) containing the tag of interest
    // Then limit tagged observations to N, plus N random counter-instances
    final List<RevisionTag> tagged = new ArrayList<>();
    final List<RevisionTag> other = new ArrayList<>();
    for (val revisionTag : observations) {
      if (revisionTag.getTag().getTagId() == arguments.getTagId()) {
        tagged.add(revisionTag);
      } else {
        other.add(revisionTag);
      }
    }

    limitObservations(tagged);
    tagged.addAll(randomObservations(other, tagged.size()));
    return tagged;
  }

  private void limitObservations(final List<RevisionTag> observations) {
    if (arguments.getObservationLimit() == Arguments.NO_LIMIT ||
        observations.size() <= arguments.getObservationLimit()) {
      return;
    }

    observations
        .removeAll(observations.subList(arguments.getObservationLimit(), observations.size()));
  }

  private List<RevisionTag> randomObservations(final List<RevisionTag> observations,
      final int count) {

    final List<RevisionTag> shuffled = new ArrayList<>(observations);
    Collections.shuffle(shuffled);
    return shuffled.subList(0, count);
  }
}

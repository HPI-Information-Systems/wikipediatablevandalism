package features.context;

import com.google.common.base.Preconditions;
import features.DeltaFeature;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import wikixmlsplit.datastructures.MyRevisionType;

@RequiredArgsConstructor
class TimeSinceLastArticle implements DeltaFeature {

  private final TemporalUnit unit;

  @Override
  public Object getValue(final List<MyRevisionType> precursors, final MyRevisionType revision) {
    val sameContributor = getContributorFilter(revision.getContributor());
    val previousContribution = precursors.stream()
        .filter(precursor -> sameContributor.test(precursor.getContributor()))
        .findFirst();
    return previousContribution.map(precursor -> timeDelta(precursor, revision)).orElse(null);
  }

  private Predicate<ContributorType> getContributorFilter(final ContributorType toCompare) {
    if (Utils.isAnonymous(toCompare)) {
      return sameIpAs(toCompare);
    }
    return sameNameAs(toCompare);
  }

  private Predicate<ContributorType> sameIpAs(final ContributorType toCompare) {
    return contributor -> contributor.getIp().equals(toCompare.getIp());
  }

  private Predicate<ContributorType> sameNameAs(final ContributorType toCompare) {
    return contributor -> contributor.getUsername().equals(toCompare.getUsername());
  }

  private long timeDelta(final MyRevisionType previous, final MyRevisionType next) {
    val past = previous.getDate().toInstant();
    val upcoming = next.getDate().toInstant();
    Preconditions.checkState(past.isBefore(upcoming), "Time delta should compare past to upcoming");
    return Duration.between(past, upcoming).get(unit);
  }
}

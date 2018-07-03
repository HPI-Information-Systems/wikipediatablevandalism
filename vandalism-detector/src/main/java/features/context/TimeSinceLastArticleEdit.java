package features.context;

import com.google.common.base.Preconditions;
import features.Feature;
import java.time.temporal.TemporalUnit;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.FeatureContext;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Time since the last edit was made by the same contributor on the same page.
 */
@RequiredArgsConstructor
class TimeSinceLastArticleEdit implements Feature {

  private final TemporalUnit unit;

  @Override
  public Object getValue(final MyRevisionType revision, FeatureContext featureContext) {
    val precursors = featureContext.getPreviousRevisions();
    val sameContributor = getContributorFilter(revision.getContributor());
    val previousContribution = precursors.stream()
        .filter(precursor -> sameContributor.test(precursor.getContributor()))
        .findFirst();
    return previousContribution.map(precursor -> timeDelta(precursor, revision)).orElse(-1l);
  }

  private Predicate<ContributorType> getContributorFilter(final ContributorType toCompare) {
    if (Utils.isAnonymous(toCompare)) {
      return sameIpAs(toCompare);
    }
    return sameNameAs(toCompare);
  }

  private Predicate<ContributorType> sameIpAs(final ContributorType toCompare) {
    return contributor -> toCompare.getIp().equals(contributor.getIp());
  }

  private Predicate<ContributorType> sameNameAs(final ContributorType toCompare) {
    return contributor -> toCompare.getUsername().equals(contributor.getUsername());
  }

  private long timeDelta(final MyRevisionType previous, final MyRevisionType next) {
    val past = previous.getDate().toInstant();
    val upcoming = next.getDate().toInstant();
    Preconditions.checkState(past.isBefore(upcoming), "Time delta should compare past to upcoming");
    return unit.between(past, upcoming);
  }
}

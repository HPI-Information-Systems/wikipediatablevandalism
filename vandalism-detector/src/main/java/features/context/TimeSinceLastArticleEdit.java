package features.context;

import com.google.common.base.Preconditions;
import features.Feature;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.val;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Time since the last edit was made by the same contributor on the same page.
 */
class TimeSinceLastArticleEdit implements Feature {

  @Override
  public Object getValue(final FeatureParameters parameters) {
    val revision = parameters.getRevision();
    val precursors = parameters.getPreviousRevisions();
    val sameContributor = getContributorFilter(revision.getContributor());
    val previousContribution = precursors.stream()
        .filter(precursor -> sameContributor.test(precursor.getContributor()))
        .findFirst();
    return previousContribution.map(precursor -> timeDelta(precursor, revision)).orElse(-1l);
  }

  private Predicate<ContributorType> getContributorFilter(final ContributorType toCompare) {
    if (BasicUtils.isAnonymous(toCompare)) {
      return sameIpAs(toCompare);
    }
    return sameNameAs(toCompare);
  }

  private Predicate<ContributorType> sameIpAs(final ContributorType toCompare) {
    return contributor -> Objects.equals(toCompare.getIp(), contributor.getIp());
  }

  private Predicate<ContributorType> sameNameAs(final ContributorType toCompare) {
    return contributor -> Objects.equals(toCompare.getUsername(), contributor.getUsername());
  }

  private long timeDelta(final MyRevisionType previous, final MyRevisionType next) {
    val past = previous.getDate().toInstant();
    val upcoming = next.getDate().toInstant();
    Preconditions.checkState(past.isBefore(upcoming), "Time delta should compare past to upcoming");
    return Duration.between(past, upcoming).toMinutes(); //TODO maybe use getSeconds() instead
  }
}

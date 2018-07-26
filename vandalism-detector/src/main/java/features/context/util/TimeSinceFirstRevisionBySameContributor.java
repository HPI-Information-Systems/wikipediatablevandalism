package features.context.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.val;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import util.BasicUtils;
import wikixmlsplit.datastructures.MyRevisionType;

public class TimeSinceFirstRevisionBySameContributor {

  @SuppressWarnings("all")
  public static double getTime(final MyRevisionType revision, final List<MyRevisionType> previousRevisions) {
    val sameContributor = getContributorFilter(revision.getContributor());
    val previousContribution = previousRevisions.stream()
        .filter(precursor -> sameContributor.test(precursor.getContributor()))
        .findFirst();
    return previousContribution.map(previousRevision -> timeDelta(previousRevision, revision)).orElse(-1l);
  }

  private static Predicate<ContributorType> getContributorFilter(final ContributorType toCompare) {
    if (BasicUtils.isAnonymous(toCompare)) {
      return sameIpAs(toCompare);
    }
    return sameNameAs(toCompare);
  }

  private static Predicate<ContributorType> sameIpAs(final ContributorType toCompare) {
    return contributor -> Objects.equals(toCompare.getIp(), contributor.getIp());
  }

  private static Predicate<ContributorType> sameNameAs(final ContributorType toCompare) {
    return contributor -> Objects.equals(toCompare.getUsername(), contributor.getUsername());
  }

  private static long timeDelta(final MyRevisionType previous, final MyRevisionType next) {
    return BasicUtils.getTimeDuration(next, previous);
  }

}

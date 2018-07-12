package matching.persistence;

import static util.PerformanceUtil.runMeasured;

import lombok.val;
import runner.Arguments;
import util.PageUtil;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.Settings;
import wikixmlsplit.api.TableMatcher;
import wikixmlsplit.datastructures.MyPageType;

/**
 * Encapsulate different methods to obtain a matching: either computed or a persisted representation
 * from disk. If the persistence path is set, missing persisted matching are computed & written.
 */
public class MatchingFacade {

  private final Arguments arguments;
  private final MatchingPersistence persistence;

  public MatchingFacade(final Arguments arguments) {
    this.arguments = arguments;
    persistence = new MatchingPersistence(arguments);
  }

  public Matching obtainMatching(final MyPageType page, final int maxRevisionId) {
    final boolean persisted = arguments.getMatchingPath() != null;
    if (persisted) {
      return readOrComputeMatching(page, maxRevisionId);
    } else {
      return computeMatching(page, maxRevisionId);
    }
  }

  private Matching readOrComputeMatching(final MyPageType page, final int maxRevisionId) {
    if (persistence.isMatchingAvailable(page)) {
      return persistence.read(page, maxRevisionId);
    }

    val matching = computeMatching(page, maxRevisionId);
    persistence.persist(page, matching, maxRevisionId);
    return matching;
  }

  private Matching computeMatching(final MyPageType page, final int maxRevisionId) {
    val matcher = new TableMatcher(Settings.ofDefault());
    val revisionIndex = PageUtil.getRevisionIndex(page, maxRevisionId);
    return runMeasured("Page matching", () -> matcher.performMatching(page, revisionIndex));
  }
}

package matching.table;

import java.util.List;
import java.util.Objects;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import wikixmlsplit.api.Entry;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Given a particular cluster and revision, find the previous revision and identify the type of
 * change made to this cluster's table.
 */
@Slf4j
class TableTransitionScanner {

  ScanResult scanEntries(final List<Entry> entries, final MyRevisionType revision) {
    Entry previous = null;
    for (final Entry current : entries) {
      if (current.getRevisionId().equals(revision.getId())) {

        val previousActive = previous != null && previous.isActive();
        val currentActive = current.isActive();

        if (previousActive) {
          return currentActive ?
              ScanResult.matched(previous, current) :  // both active
              ScanResult.removed(previous); // active -> inactive transition: removal
        } else {
          return currentActive ?
              ScanResult.added(current) : // inactive -> active transition: addition
              ScanResult.absent(); // both inactive: table stayed deleted for several revisions
        }
      }

      previous = current;
    }

    log.error("Revision {} not found in cluster history", revision.getId());
    return ScanResult.absent();
  }

  enum ScanResultType {
    ACTIVE,
    ADDED,
    REMOVED,
    ABSENT
  }

  @Value
  static class ScanResult {

    private final ScanResultType type;
    private final Entry previous;
    private final Entry current;

    private static ScanResult matched(final Entry previous, final Entry current) {
      Objects.requireNonNull(previous);
      Objects.requireNonNull(current);
      return new ScanResult(ScanResultType.ACTIVE, previous, current);
    }

    private static ScanResult removed(final Entry previous) {
      Objects.requireNonNull(previous);
      return new ScanResult(ScanResultType.REMOVED, previous, null);
    }

    private static ScanResult added(final Entry current) {
      Objects.requireNonNull(current);
      return new ScanResult(ScanResultType.ADDED, null, current);
    }

    private static ScanResult absent() {
      return new ScanResult(ScanResultType.ABSENT, null, null);
    }
  }
}

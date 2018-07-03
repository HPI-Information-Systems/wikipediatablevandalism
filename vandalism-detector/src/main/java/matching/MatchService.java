package matching;

import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import wikixmlsplit.api.Entry;
import wikixmlsplit.api.Matching;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

@Slf4j
public class MatchService {

  @Value
  @Builder
  public static class Result {

    @Singular
    private List<Match> matches;

    @Singular
    private List<WikiTable> removedTables;

    @Singular
    private List<WikiTable> addedTables;
  }

  @Value
  @Builder
  public static class Match {

    private BigInteger previousRevision;
    private WikiTable previousTable;
    private BigInteger currentRevision;
    private WikiTable currentTable;
  }

  public Result getMatchingTable(final Matching matching, final MyRevisionType revision,
      final List<MyRevisionType> revisions) {
    val entries = matching.getEntries();
    val result = Result.builder();

    for (int tableIndex = 0; tableIndex < entries.size(); ++tableIndex) {
      val entry = entries.get(tableIndex);

      Preconditions.checkState(!entry.isEmpty(), "Table history cannot be empty");
      val first = entry.get(0);
      if (revision.getId().compareTo(first.getRevisionId()) < 0) {
        // Entire table history starts after our revision
        continue;
      }

      if (entry.size() > 1) {
        val last = entry.get(entry.size() - 1);
        if (last.getRevisionId().compareTo(revision.getId()) < 0) {
          // Entire table history ends before our revision
          continue;
        }
      }


      val prevAndCurrent = scanEntries(entry, revision);

      if (prevAndCurrent == null) {
        continue; // FIXME
      }

      if (prevAndCurrent.getRight() == null) {
        result.removedTable(prevAndCurrent.getLeft().getTable());
      } else if (prevAndCurrent.getLeft() == null) {
        result.addedTable(prevAndCurrent.getRight().getTable());
      } else {
        result.match(Match.builder()
            .previousRevision(prevAndCurrent.getLeft().getRevisionId())
            .previousTable(prevAndCurrent.getLeft().getTable())
            .currentRevision(prevAndCurrent.getRight().getRevisionId())
            .currentTable(prevAndCurrent.getRight().getTable())
            .build());
      }
    }

    return result.build();
  }

  private Pair<Entry, Entry> scanEntries(final List<Entry> entries, final MyRevisionType revision) {
    Entry previous = null;

    for (int index = 0; index < entries.size(); ++index) {
      val current = entries.get(index);

      if (current.getRevisionId().equals(revision.getId())) {

        if (!current.isActive()) {
          log.trace("Table {} was not active in revision {}", current.getTable().caption,
              revision.getId());
          return Pair.of(previous, null);
        }

        if (previous == null) {
          throw new IllegalArgumentException(
              "Revision " + revision.getId() + " does not seem to have a predecessor");
        }

        return Pair.of(previous, current);
      }

      previous = current;
    }

    if (entries.isEmpty()) {
      log.warn("Empty entries list for revision {}", revision.getId());
      throw new IllegalStateException("Empty entries for revision " + revision.getId());
    } else {
      return null;
      //throw new IllegalStateException(String.format("No record for table %s in revision %s", entries.get(0).getTable().caption, revision.getId()));
    }
  }
}

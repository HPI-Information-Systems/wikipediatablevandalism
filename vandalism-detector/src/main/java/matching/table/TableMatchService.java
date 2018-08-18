package matching.table;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import matching.table.TableMatchResult.TableMatchResultBuilder;
import matching.table.TableTransitionScanner.ScanResult;
import wikixmlsplit.api.Entry;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.TableParser;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Given a set of table clusters, identify table additions, removals and modifications from the
 * previous to the current revision.
 */
@Slf4j
public class TableMatchService {

  private final TableParser tableHelper = TableParser.instance();
  private final TableTransitionScanner scanner = new TableTransitionScanner();

  public TableMatchResult getMatchingTable(final Matching matching, final MyRevisionType revision,
      final MyRevisionType previousRevision) {

    if (previousRevision == null) {
      return allTablesAdded(revision);
    }

    val clusters = matching.getEntries();
    val result = TableMatchResult.builder();
    for (int clusterIndex = 0; clusterIndex < clusters.size(); ++clusterIndex) {
      final List<Entry> cluster = clusters.get(clusterIndex);
      if (skipCluster(revision, cluster)) {
        continue;
      }

      final ScanResult scanResult = scanner.scanEntries(cluster, revision);
      addToResult(result, clusterIndex, scanResult, revision, previousRevision);
    }

    return result.build();
  }

  private boolean skipCluster(final MyRevisionType revision, final List<Entry> cluster) {
    val first = cluster.get(0);
    if (revision.getId().compareTo(first.getRevisionId()) < 0) {
      // Entire table history starts after current revision
      return true;
    }

    if (cluster.size() > 1) {
      val last = cluster.get(cluster.size() - 1);
      // Entire table history may end before current revision
      return last.getRevisionId().compareTo(revision.getId()) < 0;
    }

    return false;
  }

  private TableMatchResult allTablesAdded(final MyRevisionType revision) {
    log.debug("All tables of revision {} were added", revision.getId());
    final List<WikiTable> tables = tableHelper.parseTables(revision, true); // FIXME
    return TableMatchResult.builder().addedTables(tables).build();
  }

  private WikiTable getTable(final MyRevisionType revision, final Entry entry) {
    final List<WikiTable> tables = tableHelper.parseTables(revision, true);  // FIXME
    return tables.get(entry.getPosition());
  }

  private void addToResult(final TableMatchResultBuilder result,
      final int clusterIndex,
      final ScanResult scanResult,
      final MyRevisionType revision, final MyRevisionType previousRevision) {

    switch (scanResult.getType()) {
      case ADDED:
        result.addedTable(getTable(revision, scanResult.getCurrent()));
        break;

      case REMOVED:
        result.removedTable(getTable(previousRevision, scanResult.getPrevious()));
        break;

      case ACTIVE:
        result.match(toMatch(clusterIndex, scanResult, revision, previousRevision));
        break;

      case ABSENT:
        log.debug("Table was absent between revisions {} and {}",
            previousRevision.getId(), revision.getId());
        break;

      default:
        throw new IllegalArgumentException(scanResult.getType().name());
    }
  }

  private TableMatch toMatch(final int clusterIndex,
      final ScanResult result,
      final MyRevisionType currentRevision,
      final MyRevisionType previousRevision) {

    return TableMatch.builder()
        .currentRevision(currentRevision.getId())
        .currentTable(getTable(currentRevision, result.getCurrent()))
        .previousRevision(previousRevision.getId())
        .previousTable(getTable(previousRevision, result.getPrevious()))
        .clusterIndex(clusterIndex)
        .build();
  }
}

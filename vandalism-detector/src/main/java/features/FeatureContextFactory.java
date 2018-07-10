package features;

import static util.PerformanceUtil.runMeasured;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import matching.row.RowMatchResult;
import matching.row.RowMatchService;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import matching.table.TableMatchService;
import model.FeatureContext;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.Settings;
import wikixmlsplit.api.TableMatcher;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

@Slf4j
class FeatureContextFactory {

  FeatureContext create(final MyPageType page, final int revisionIndex) {
    val revision = page.getRevisions().get(revisionIndex);
    val matching = getMatching(page, revisionIndex);
    val tableMatchResult = getTableMatching(revision, matching);
    val selectedMatch = selectMatch(tableMatchResult);

    return FeatureContext.builder()
        .page(page)
        .previousRevisions(previousRevisions(page, revisionIndex))
        .result(tableMatchResult)
        .relevantMatch(selectedMatch)
        .rowMatchResult(getRowMatching(selectedMatch))
        .build();
  }

  private List<MyRevisionType> previousRevisions(final MyPageType page, final int revisionIndex) {
    val n = revisionIndex; // TODO currently all previousRevisions
    return Lists.reverse(page.getRevisions().subList(revisionIndex - n,
        revisionIndex)); // reverse list -> index = 0 is the previous revision, index = 1 is the one before, etc.;
  }

  private Matching getMatching(final MyPageType page, final int revisionIndex) {
    val matcher = new TableMatcher(Settings.ofDefault());
    return runMeasured("Page matching", () -> matcher.performMatching(page, revisionIndex));
  }

  private TableMatch selectMatch(final TableMatchResult matches) {
    for (final TableMatch m : matches.getMatches()) {
      if (!m.getPreviousTable().equals(m.getCurrentTable())) {
        // TODO consider similarity < 1
        return m;
      }
    }

    log.warn("All matched tables identical");
    return null;
  }

  private TableMatchResult getTableMatching(final MyRevisionType revision,
      final Matching matching) {
    val tableMatchService = new TableMatchService();
    return tableMatchService.getMatchingTable(matching, revision);
  }

  private RowMatchResult getRowMatching(final TableMatch match) {
    val rowMatchService = new RowMatchService();
    return match == null ? null :
        rowMatchService.matchRows(match.getPreviousTable(), match.getCurrentTable());
  }
}

package features;

import static util.PageUtil.getRevisionIndex;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import matching.row.RowMatchResult;
import matching.row.RowMatchService;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import matching.table.TableMatchService;
import model.FeatureParameters;
import util.BasicUtils;
import util.PageUtil;
import wikixmlsplit.api.Matching;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

@Slf4j
class FeatureParametersFactory {

  FeatureParameters create(final MyPageType page, final MyRevisionType revision,
      final Matching matching) {

    val tableMatchResult = getTableMatching(page, revision, matching);
    val selectedMatch = selectMatch(tableMatchResult);
    final List<MyRevisionType> previousRevisions = previousRevisions(page, revision);

    return FeatureParameters.builder()
        .page(page)
        .revision(revision)
        .previousRevision(BasicUtils.getPreviousRevision(previousRevisions))
        .previousRevisions(previousRevisions)
        .result(tableMatchResult)
        .relevantMatch(selectedMatch)
        .rowMatchResult(getRowMatching(selectedMatch))
        .build();
  }

  private List<MyRevisionType> previousRevisions(final MyPageType page,
      final MyRevisionType revision) {

    val revisionIndex = getRevisionIndex(page, revision);
    val n = revisionIndex; // TODO currently all previousRevisions
    return Lists.reverse(page.getRevisions().subList(revisionIndex - n,
        revisionIndex)); // reverse list -> index = 0 is the previous revision, index = 1 is the one before, etc.;
  }

  private TableMatch selectMatch(final TableMatchResult matches) {
    if (matches.getMatches().isEmpty()) {
      return null;
    }

    for (final TableMatch m : matches.getMatches()) {
      if (!m.getPreviousTable().equals(m.getCurrentTable())) {
        // TODO consider similarity < 1
        return m;
      }
    }

    log.warn("All matched tables identical");
    return null;
  }

  private TableMatchResult getTableMatching(final MyPageType page, final MyRevisionType revision,
      final Matching matching) {
    val tableMatchService = new TableMatchService();
    val previousRevision = PageUtil.findPreviousRevision(page, revision);
    return tableMatchService.getMatchingTable(matching, revision, previousRevision);
  }

  private RowMatchResult getRowMatching(final TableMatch match) {
    val rowMatchService = new RowMatchService();
    return match == null ? null :
        rowMatchService.matchRows(match.getPreviousTable(), match.getCurrentTable());
  }
}

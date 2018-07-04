package features.content;

import features.Feature;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import matching.row.RowMatch;
import matching.row.RowMatchService;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import matching.table.TableMatchService;
import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;

@RequiredArgsConstructor
class RankChange implements Feature {

  private static final double SIMILARITY_THRESHOLD = 0.25;

  private final TableMatchService matchService;
  private final RowMatchService rowMatchService;

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext context) {
    final TableMatchResult matches = matchService.getMatchingTable(context.getMatching(), revision);
    final TableMatch change = ContentUtil.selectChange(matches);

    if (change == null) {
      // All tables identical - no rank change
      return false;
    }

    // If we have a rank change
    // (1) all rows of previous need to have a very similar row in current table
    // (2) the rows of previous table do not appear in the same order
    // TODO reconsider - the row matching is not necessarily injective!
    final List<RowMatch> matchedRows = rowMatchService
        .matchRows(change.getPreviousTable(), change.getCurrentTable())
        .getMatches();

    val allMatched = matchedRows.stream().allMatch(this::isExceedingThreshold);
    if (!allMatched) {
      return false;
    }

    int prev = matchedRows.get(0).getMatchedIndex();
    for (final RowMatch m : matchedRows.subList(1, matchedRows.size())) {
      if (prev >= m.getMatchedIndex()) {
        return true;
      }
      prev = m.getMatchedIndex();
    }

    return false;
  }

  private boolean isExceedingThreshold(final RowMatch rowMatch) {
    return rowMatch.getSimilarity() > SIMILARITY_THRESHOLD;
  }
}

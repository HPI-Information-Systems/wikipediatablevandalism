package features.content.util.table;

import features.Feature;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import matching.row.RowMatch;
import matching.table.TableMatch;
import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;

@RequiredArgsConstructor
public class RankChange implements Feature {

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext context) {
    final TableMatch change = context.getRelevantMatch();

    if (change == null) {
      // All tables identical - no rank change
      return false;
    }

    // If we have a rank change
    // (1) all rows of previous need to have a very similar row in current table
    // (2) the rows of previous table do not appear in the same order
    // TODO reconsider - the row matching is not necessarily injective!
    final List<RowMatch> matchedRows = context.getRowMatchResult().getMatches();

    if (matchedRows.isEmpty()) {
      return false;
    }

    val allMatched = matchedRows.size() == change.getCurrentTable().getRows().size();
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
}

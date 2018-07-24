package features.content.util.table;

import features.Feature;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import matching.row.RowMatch;
import matching.table.TableMatch;
import model.FeatureParameters;

@RequiredArgsConstructor
public class RankChange implements Feature {

  @Override
  public double getValue(final FeatureParameters parameters) {
    final TableMatch change = parameters.getRelevantMatch();

    if (change == null) {
      // All tables identical - no rank change
      return 0;
    }

    // If we have a rank change
    // (1) all rows of previous need to have a very similar row in current table
    // (2) the rows of previous table do not appear in the same order
    // TODO reconsider - the row matching is not necessarily injective!
    final List<RowMatch> matchedRows = parameters.getRowMatchResult().getMatches();

    if (matchedRows.isEmpty()) {
      return 0;
    }

    val allMatched = matchedRows.size() == change.getCurrentTable().getRows().size();
    if (!allMatched) {
      return 0;
    }

    int prev = matchedRows.get(0).getMatchedIndex();
    for (final RowMatch m : matchedRows.subList(1, matchedRows.size())) {
      if (prev >= m.getMatchedIndex()) {
        return 1;
      }
      prev = m.getMatchedIndex();
    }

    return 0;
  }
}

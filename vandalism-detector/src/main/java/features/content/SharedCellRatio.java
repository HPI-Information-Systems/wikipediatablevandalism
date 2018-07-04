package features.content;

import com.google.common.collect.Sets;
import com.google.common.math.Stats;
import features.Feature;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.val;
import matching.table.TableMatch;
import matching.table.TableMatchService;
import model.FeatureContext;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

@RequiredArgsConstructor
class SharedCellRatio implements Feature {

  private final TableMatchService matchService;

  @Override
  public Object getValue(final MyRevisionType revision, final FeatureContext context) {
    val matches = matchService.getMatchingTable(context.getMatching(), revision);
    final List<Double> values = new ArrayList<>();
    for (val match : matches.getMatches()) {
      values.add(processMatch(match));
    }

    if (values.isEmpty()) {
      return 1d;
    }

    return Stats.meanOf(values);
  }

  private double processMatch(final TableMatch match) {
    val currentCellValues = getCellValues(match.getCurrentTable());
    val previousCellValues = getCellValues(match.getPreviousTable());
    val sharedValues = Sets.intersection(currentCellValues, previousCellValues);
    return (double) sharedValues.size() / currentCellValues.size();
  }

  private Set<String> getCellValues(final WikiTable table) {
    final Set<String> values = new HashSet<>();
    for (val row : table.getRows()) {
      for (int cell = 0; cell < row.getSize(); ++cell) {
        values.add(row.getValue(cell));
      }
    }
    return values;
  }
}

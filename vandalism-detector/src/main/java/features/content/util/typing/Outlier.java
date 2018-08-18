package features.content.util.typing;

import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.math.Stats;
import features.Feature;
import features.content.util.typing.DataTypeInference.CellDataType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.Row;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * For each row or column, find a set of newly introduced values. Then check if those new values fit
 * into the series of values that remained fixed.
 */
@RequiredArgsConstructor
public class Outlier implements Feature {

  public interface RowProvider {

    List<Row> getValue(WikiTable table);
  }

  private final RowProvider rowProvider;
  private final DataTypeInference cellTyper;

  @Override
  public double getValue(final FeatureParameters parameters) {
    val match = parameters.getRelevantMatch();
    if (match == null) {
      return 0;
    }

    val current = rowProvider.getValue(match.getCurrentTable());
    val previous = rowProvider.getValue(match.getPreviousTable());

    if (current.size() != previous.size()) {
      return 0;
    }

    for (int index = 0; index < current.size(); ++index) {
      if (findOutlier(current.get(index), previous.get(index))) {
        return 1;
      }
    }
    return 0;
  }

  private boolean findOutlier(final Row current, final Row previous) {
    final List<Double> currentNumbers = extractNumericValues(current);
    final List<Double> previousNumbers = extractNumericValues(previous);
    final Set<Double> newValues = getNewValues(currentNumbers, previousNumbers);

    if (previousNumbers.isEmpty() && !currentNumbers.isEmpty()) {
      // Only added numeric content
      return true;
    }

    if (previousNumbers.isEmpty()) {
      // No numbers previously, no numbers now
      return false;
    }

    for (final double newValue : newValues) {
      if (isOutlier(previousNumbers, newValue)) {
        return true;
      }
    }

    return false;
  }

  private List<Double> extractNumericValues(final Row row) {
    final List<Double> values = new ArrayList<>(row.getSize());
    for (final Cell cell : row.getValues()) {
      val result = cellTyper.guess(cell.getValue());
      if (result.getType() == CellDataType.Number) {
        values.add(result.getAsDouble());
      }
    }
    return values;
  }

  private Set<Double> getNewValues(final List<Double> current, final List<Double> previous) {
    final Set<Double> currentValues = new HashSet<>(current);
    final Set<Double> previousValues = new HashSet<>(previous);
    return Sets.difference(currentValues, previousValues);
  }

  static boolean isOutlier(final List<Double> values, final double toCheck) {
    if (values.size() < 2) {
      return false;
    }

    final double mean = Stats.meanOf(values);
    final double deviation = Stats.of(values).populationStandardDeviation();
    final Range<Double> range = Range.closed(mean - 3 * deviation, mean + 3 * deviation);
    return !range.contains(toCheck);
  }
}

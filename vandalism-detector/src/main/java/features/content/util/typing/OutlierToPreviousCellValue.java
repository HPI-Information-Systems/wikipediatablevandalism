package features.content.util.typing;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import features.Feature;
import features.content.util.typing.DataTypeInference.CellDataType;
import features.content.util.typing.DataTypeInference.DataTypeInferenceResult;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Value;
import matching.table.TableMatch;
import model.FeatureParameters;
import wikixmlsplit.api.Entry;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.TableParser;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.Row;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Find all changed cells and check if the new value is an outlier to the previous values of the
 * same cell.
 *
 * <p>This is intentionally based on set logic: collect distinct values that cell had previously.
 * It should measure outlier with respect to other edits of that very same cell. It is not
 * meaningful to weight cell values towards a predominant value, as in "the value of this cell has
 * not changed in the last 100 revisions altering that table, thus another legit value can never
 * appear".</p>
 */
public class OutlierToPreviousCellValue implements Feature {

  /**
   * Collect at max this many different previous cell values per change.
   */
  private static final int MAX_OBSERVATIONS_PER_CHANGE = 10;

  /**
   * Parse at most this many revisions when looking for values that cell possessed in the past.
   */
  private static final int MAX_REVISIONS_TO_CHECK = 500;

  private final TableParser parser = TableParser.instance();
  private final DataTypeInference typer = new DataTypeInference();


  @Override
  public double getValue(FeatureParameters parameters) {
    return hasCellOutlier(parameters) ? 1 : 0;
  }

  private boolean hasCellOutlier(final FeatureParameters parameters) {
    for (final TableMatch match : parameters.getChangedTables()) {

      if (match.getPreviousTable().equals(match.getCurrentTable())) {
        continue;
      }

      if (!sameSize(match.getPreviousTable(), match.getCurrentTable())) {
        continue;
      }

      Set<Change> changes = changesOf(match.getPreviousTable(), match.getCurrentTable());
      SetMultimap<Change, String> prevStates = recentValuesOfChangedCells(parameters, match,
          changes);

      for (Change change : changes) {
        DataTypeInferenceResult previousType = typer.guess(change.getPreviousValue());
        DataTypeInferenceResult currentType = typer.guess(change.getCurrentValue());

        if (previousType.getType() != currentType.getType()) {
          return true;
        }

        if (currentType.getType() == CellDataType.Number) {
          double currentValue = currentType.getAsDouble();
          Set<Double> prevValues = prevStates.get(change).stream()
              .map(typer::guess)
              .filter(t -> t.getType() == CellDataType.Number)
              .map(DataTypeInferenceResult::getAsDouble)
              .collect(toSet());

          boolean outlier = Outlier.isOutlier(new ArrayList<>(prevValues), currentValue);
          if (outlier) {
            return true;
          }
        }
      }

    }

    return false;
  }

  private Set<Change> changesOf(final WikiTable previous, final WikiTable current) {
    int rows = previous.getRows().size();
    Set<Change> changes = new HashSet<>();
    for (int rowIndex = 0; rowIndex < rows; ++rowIndex) {
      List<Cell> previousRow = previous.getRows().get(rowIndex).getValues();
      List<Cell> currentRow = current.getRows().get(rowIndex).getValues();

      if (previousRow.size() != currentRow.size()) {
        continue;
      }

      for (int columnIndex = 0; columnIndex < previousRow.size(); ++columnIndex) {

        String previousValue = previousRow.get(columnIndex).getValue();
        String currentValue = currentRow.get(columnIndex).getValue();

        if (!previousValue.equals(currentValue)) {
          changes.add(Change.builder()
              .row(rowIndex)
              .column(columnIndex)
              .previousValue(previousValue)
              .currentValue(currentValue)
              .build());
        }
      }
    }
    return changes;
  }

  @Value
  @Builder
  private static class Change {

    private int row;
    private int column;
    private String previousValue;
    private String currentValue;
  }

  // Get at most 10 previous values (t - 1, t - 2, t -3, ...)
  private SetMultimap<Change, String> recentValuesOfChangedCells(
      final FeatureParameters parameters,
      final TableMatch match,
      final Set<Change> changes) {

    final WikiTable table = match.getCurrentTable();
    final Matching matching = parameters.getMatching();
    final List<Entry> cluster = matching.getEntries().get(match.getClusterIndex());

    final SetMultimap<Change, String> previousStates = HashMultimap.create();

    List<BigInteger> prevRevIds = parameters.getPreviousRevisions().stream()
        .map(MyRevisionType::getId).collect(toList());
    List<BigInteger> clusterRevIds = cluster.stream().map(Entry::getRevisionId).collect(toList());

    int clusterUpperBound =
        Collections.binarySearch(clusterRevIds, parameters.getRevision().getId()) - 1;
    int clusterLowerBound = Math.max(0, clusterUpperBound - MAX_REVISIONS_TO_CHECK);

    for (int index = clusterUpperBound; index >= clusterLowerBound; --index) {
      Entry entry = cluster.get(index);
      if (!entry.isActive()) {
        continue;
      }

      int prevIndex = Collections
          .binarySearch(prevRevIds, entry.getRevisionId(), Comparator.reverseOrder());
      MyRevisionType prevRevision = parameters.getPreviousRevisions().get(prevIndex);

      WikiTable prevTable = parser.parseTable(prevRevision, entry.getPosition(), true);

      if (prevTable == null || prevTable.equals(table)) {
        continue;
      }

      List<Row> currentRows = table.getRows();
      List<Row> prevRows = prevTable.getRows();

      if (prevRows.size() != currentRows.size()) {
        continue;
      }

      for (final Change change : changes) {
        Row prevRow = prevRows.get(change.getRow());
        Row currentRow = currentRows.get(change.getRow());

        if (prevRow.getSize() != currentRow.getSize()) {
          continue;
        }

        if (change.getColumn() < prevRow.getSize()) {
          final String value = prevRow.getValue(change.getColumn());
          previousStates.put(change, value);
        }
      }

      boolean complete = true;
      for (Change change : previousStates.keySet()) {
        complete &= previousStates.get(change).size() >= MAX_OBSERVATIONS_PER_CHANGE;
      }
      if (complete) {
        break;
      }

    }

    return previousStates;
  }

  private boolean sameSize(final WikiTable t1, final WikiTable t2) {
    return Comparator.<WikiTable, Integer>comparing(table -> table.getRows().size())
        .thenComparing(table -> table.getColumns().size()).compare(t1, t2) == 0;
  }
}

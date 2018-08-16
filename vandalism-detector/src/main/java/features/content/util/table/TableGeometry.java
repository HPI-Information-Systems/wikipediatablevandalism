package features.content.util.table;

import features.Feature;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.val;
import matching.table.TableMatch;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Measure changes in table geometry for matches, aka "number of rows added" and similar.
 *
 * <p>Previously this feature also took additions and removals into account. This is deemed not
 * helpful, since inferring sense between "1 - 10 columns added / removed" is much easier than
 * reasoning about a value range of +-400 (in case entire pages have been blanked / restored /
 * created).</p>
 *
 * <p>Retouching multiple tables may cause the size change to level out, so it is more an edit
 * tendency (are more cells deleted than added?). Benefit: in case that cells / rows / columns might
 * be moved in between matched tables, the size change is zero.</p>
 */
public class TableGeometry implements Feature {

  @Getter
  public enum Measure {
    Columns(table -> table.getColumns().size()),
    Rows(table -> table.getRows().size()),
    Product(table -> table.getRows().size() * table.getColumns().size());

    private final ValueFunction f;

    Measure(final ValueFunction f) {
      this.f = f;
    }
  }

  @FunctionalInterface
  interface ValueFunction {

    double apply(WikiTable table);
  }

  private final ValueFunction valueFunction;

  public TableGeometry(final Measure measure) {
    valueFunction = measure.getF();
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val results = parameters.getResult();

    final List<Double> sizeChanges = new ArrayList<>(results.getMatches().size());
    for (val result : results.getMatches()) {
      sizeChanges.add(getSizeChange(result));
    }

    double result = 0;
    for (val d : sizeChanges) {
      result += d;
    }
    return result;
  }

  private double getSizeChange(final TableMatch match) {
    val d1 = valueFunction.apply(match.getPreviousTable());
    val d2 = valueFunction.apply(match.getCurrentTable());
    return d2 - d1;
  }
}

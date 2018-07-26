package features.content.wikisyntax;

import static java.util.stream.Collectors.toList;

import features.Feature;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import matching.table.TableMatch;
import model.FeatureParameters;
import org.apache.commons.collections4.CollectionUtils;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Model impact of number of occurrences of specific template instances. The size of the disjunction
 * should reflect the size of the change no matter if there has been bulk removal, addition, or
 * modification. Latter is of special importance and would be counted twice.
 */
abstract class TemplateUseImpact implements Feature {

  @Override
  public double getValue(final FeatureParameters parameters) {
    final TableMatch match = parameters.getRelevantMatch();

    if (match == null) {
      return 0;
    }

    final List<String> prev = extractTemplatesFromTable(match.getPreviousTable());
    final List<String> curr = extractTemplatesFromTable(match.getCurrentTable());
    final Collection<String> diff = CollectionUtils.disjunction(prev, curr);
    return diff.size();
  }

  protected List<String> extractTemplatesFromTable(final WikiTable table) {
    return table.getRows().stream()
        .flatMap(row -> row.getValues().stream())
        .flatMap(this::extractTemplatesFromCell)
        .collect(toList());
  }

  protected abstract Stream<String> extractTemplatesFromCell(Cell cell);
}

package features.content.wikisyntax;

import static features.content.util.language.regex.RegexUtil.countMatches;
import static java.util.stream.Collectors.toList;

import features.Feature;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.val;
import matching.table.TableMatch;
import model.FeatureParameters;
import org.apache.commons.collections4.CollectionUtils;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Model impact of number of occurrences of specific template instances.
 */
abstract class TemplateUseImpact implements Feature {

  @Override
  public double getValue(final FeatureParameters parameters) {
    final TableMatch match = parameters.getRelevantMatch();

    if (match == null) {
      return 0;
    }

    val previousMatches = countTemplatesOf(match.getPreviousTable());
    val matches = countTemplatesOf(match.getCurrentTable());
    val previousMatchCount = previousMatches > 0 ? previousMatches : 1;
    return ((double) (matches - previousMatches) / previousMatchCount);
  }

  protected long countTemplatesOf(final WikiTable table) {
    return table.getRows().stream()
        .flatMap(row -> row.getValues().stream())
        .flatMap(this::extractTemplatesFromCell)
        .count();
  }

  protected abstract Stream<String> extractTemplatesFromCell(Cell cell);
}

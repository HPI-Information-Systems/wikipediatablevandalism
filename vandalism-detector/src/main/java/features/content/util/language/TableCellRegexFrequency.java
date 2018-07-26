package features.content.util.language;

import static features.content.util.language.regex.RegexUtil.countMatches;
import static util.DiffUtil.diffCells;

import com.google.common.collect.HashMultiset;
import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.val;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.Cell;

public class TableCellRegexFrequency implements Feature {

  private final Set<Pattern> regularExpressions;

  public TableCellRegexFrequency(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val cells = diffCells(parameters);
    val values = cells.stream().map(Cell::getValue)
        .collect(Collectors.toCollection(HashMultiset::create));

    val matches = countMatches(this.regularExpressions, values);
    return cells.size() > 0 ? ((double) matches / cells.size()) : 0;
  }
}

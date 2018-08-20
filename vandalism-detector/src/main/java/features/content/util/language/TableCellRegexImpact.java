package features.content.util.language;

import static features.content.util.language.regex.RegexUtil.countMatches;
import static util.CellExtractor.extractCells;

import com.google.common.collect.HashMultiset;
import features.Feature;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;
import wikixmlsplit.renderer.wikitable.Cell;

public class TableCellRegexImpact implements Feature {

  private final Set<Pattern> regularExpressions;

  public TableCellRegexImpact(Set<Pattern> regularExpressions) {
    this.regularExpressions = regularExpressions;
  }

  @Override
  public double getValue(final FeatureParameters parameters) {
    val previousCells = extractCells(BasicUtils.getPreviousChangedTables(parameters));
    val cells = extractCells(BasicUtils.getCurrentChangedTables(parameters));

    val previousValues = previousCells.stream()
        .map(Cell::getValue)
        .collect(Collectors.toCollection(HashMultiset::create));

    val values = cells.stream()
        .map(Cell::getValue)
        .collect(Collectors.toCollection(HashMultiset::create));

    val previousMatches = countMatches(this.regularExpressions, previousValues);
    val matches = countMatches(this.regularExpressions, values);
    val previousMatchCount = previousMatches > 0 ? previousMatches : 1;
    return ((double) (matches - previousMatches) / previousMatchCount);
  }
}

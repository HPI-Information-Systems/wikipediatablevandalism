package features.content.wikisyntax;

import features.Feature;
import java.util.HashSet;
import java.util.Set;
import matching.table.TableMatch;
import model.FeatureParameters;
import util.AttributeUtil;
import wikixmlsplit.renderer.wikitable.Attribute;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.Row;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class AddedInvalidAttributes implements Feature {

  @Override
  public double getValue(FeatureParameters parameters) {

    for (final WikiTable table : parameters.getResult().getAddedTables()) {
      if (containsInvalidAttribute(collectAttributes(table))) {
        return 1;
      }
    }

    for (final TableMatch match : parameters.getChangedTables()) {
      final Set<Attribute> previous = collectAttributes(match.getPreviousTable());
      final Set<Attribute> current = collectAttributes(match.getCurrentTable());
      current.removeAll(previous);
      if (containsInvalidAttribute(current)) {
        return 1;
      }
    }

    return 0;
  }

  private Set<Attribute> collectAttributes(final WikiTable table) {
    final Set<Attribute> attributes = new HashSet<>(table.attributes);
    for (final Row row : table.getRows()) {
      attributes.addAll(row.getAttributes());
      for (final Cell cell : row.getValues()) {
        attributes.addAll(cell.getAttributes());
      }
    }
    return attributes;
  }

  private boolean containsInvalidAttribute(final Set<Attribute> attributes) {
    for (final Attribute a : attributes) {
      if (!AttributeUtil.isValidAttribute(a)) {
        return true;
      }
    }
    return false;
  }
}

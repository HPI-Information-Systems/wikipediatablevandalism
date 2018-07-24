package mock;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import wikixmlsplit.renderer.wikitable.Attribute;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class MockTables {

  public static final WikiTable NEPAL_13809326_Table_0 = new WikiTable(
      emptyTableAttributes(),
      "Urban Population",
      Lists.list(
          Lists.list(
              new Cell("Community"),
              new Cell("District"),
              new Cell("Pop. 1991<sup>1</sup>"),
              new Cell("Pop. 2001"),
              new Cell("Average growthrate"),
              new Cell("proj. 2005")),
          Lists.list(
              new Cell("Kathmandu"),
              new Cell("Kathmandu"),
              new Cell("414.264"),
              new Cell("671.846"),
              new Cell("4,7"),
              new Cell("807.300")),
          Lists.list(
              new Cell("Lalitpur"),
              new Cell("Lalitpur"),
              new Cell("117.203"),
              new Cell("162.991"),
              new Cell("3.4"),
              new Cell("190.900")),
          Lists.list(
              new Cell("Pokhara"),
              new Cell("Kaski"),
              new Cell("95.311"),
              new Cell("156.312"),
              new Cell("5,0"),
              new Cell("190.900"))),
      emptyRowAttributes(4),
      false);

  public static final WikiTable NEPAL_13809566_Table_0 = new WikiTable(
      emptyTableAttributes(),
      "Urban Population",
      Lists.list(
          Lists.list(
              new Cell("Community"),
              new Cell("District"),
              new Cell("Pop. 1991<sup>1</sup>"),
              new Cell("Pop. 2001"),
              new Cell("Average growthrate"),
              new Cell("proj. 2005")),
          Lists.list(
              new Cell("purple"),
              new Cell("Kathmandu"),
              new Cell("414.264"),
              new Cell("671.846"),
              new Cell("4,7"),
              new Cell("807.300")),
          Lists.list(
              new Cell("duckie"),
              new Cell("Lalitpur"),
              new Cell("117.203"),
              new Cell("162.991"),
              new Cell("3.4"),
              new Cell("190.900")),
          Lists.list(
              new Cell("i am dumb"),
              new Cell("Kaski"),
              new Cell("95.311"),
              new Cell("156.312"),
              new Cell("5,0"),
              new Cell("190.900"))),
      emptyRowAttributes(4),
      false);

  public static final WikiTable NEPAL_13809326_Table_0_ADDED_SYNTAX = new WikiTable(
      emptyTableAttributes(),
      "Urban Population",
      Lists.list(
          Lists.list(
              new Cell("Community"),
              new Cell("District"),
              new Cell("Pop. 1991<sup>1</sup>"),
              new Cell("<b>Pop. 2001</b>"),
              new Cell("Average growthrate"),
              new Cell("proj. 2005")),
          Lists.list(
              new Cell("purple"),
              new Cell("[[Kathmandu]]"),
              new Cell("414.264"),
              new Cell("671.846"),
              new Cell("4,7"),
              new Cell("807.300")),
          Lists.list(
              new Cell("duckie"),
              new Cell("Lalitpur"),
              new Cell("117.203"),
              new Cell("162.991"),
              new Cell("3.4"),
              new Cell("190.900")),
          Lists.list(
              new Cell("i am dumb"),
              new Cell("Kaski"),
              new Cell("95.311"),
              new Cell("156.312"),
              new Cell("5,0"),
              new Cell("190.900"))),
      emptyRowAttributes(4),
      false);

  private static List<Set<Attribute>> emptyRowAttributes(final int rowCount) {
    return Stream.generate(Collections::<Attribute>emptySet)
        .limit(rowCount)
        .collect(toList());
  }

  private static Set<Attribute> emptyTableAttributes() {
    return Collections.emptySet();
  }
}

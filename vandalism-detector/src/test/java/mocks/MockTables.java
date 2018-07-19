package mocks;

import org.assertj.core.util.Lists;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class MockTables {

  public static final WikiTable NEPAL_13809326_Table_0 = new WikiTable(
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
      false);

  public static final WikiTable NEPAL_13809566_Table_0 = new WikiTable(
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
      false);
}

package util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.List;
import java.util.stream.Collectors;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class CellExtractor {

  public static Multiset<Cell> extractCells(List<WikiTable> tables) {
    return tables.stream()
        .flatMap(table -> extractCells(table).stream())
        .collect(Collectors.toCollection(HashMultiset::create));
  }

  public static HashMultiset<Cell> extractCells(WikiTable tables) {
    return tables.getRows().stream()
        .flatMap(row -> row.getValues().stream())
        .collect(Collectors.toCollection(HashMultiset::create));
  }


  private CellExtractor() {
  }
}

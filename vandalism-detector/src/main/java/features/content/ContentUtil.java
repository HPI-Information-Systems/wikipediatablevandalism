package features.content;

import lombok.extern.slf4j.Slf4j;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import wikixmlsplit.renderer.wikitable.WikiTable;

@Slf4j
class ContentUtil {

  static TableMatch selectChange(final TableMatchResult matches) {
    for (final TableMatch m : matches.getMatches()) {
      if (!m.getPreviousTable().equals(m.getCurrentTable())) {
        // TODO consider similarity < 1
        return m;
      }
    }

    log.warn("All matched tables identical");
    return null;
  }

  static boolean tablesOfEqualDimension(WikiTable tableA, WikiTable tableB) {
    return tableA.getRows().size() == tableB.getRows().size() &&
        tableA.getColumns().size() == tableB.getColumns().size();
  }
}

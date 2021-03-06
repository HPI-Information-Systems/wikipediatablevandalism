package matching.table;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Represent the changes between two revisions with instances of {@link WikiTable}.
 */
@Value
@Builder
public class TableMatchResult {

  @Singular
  private List<TableMatch> matches;

  @Singular
  private List<WikiTable> removedTables;

  @Singular
  private List<WikiTable> addedTables;
}

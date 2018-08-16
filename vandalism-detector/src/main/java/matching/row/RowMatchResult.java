package matching.row;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import wikixmlsplit.renderer.wikitable.Row;

@Value
@Builder
public class RowMatchResult {

  @Singular
  private final List<RowMatch> matches;

  @Singular
  private final List<Row> addedRows;

  @Singular
  private final List<Row> deletedRows;

}

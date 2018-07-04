package matching.table;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Value;
import wikixmlsplit.renderer.wikitable.WikiTable;

@Value
@Builder
public class TableMatch {

  private BigInteger previousRevision;
  private WikiTable previousTable;
  private BigInteger currentRevision;
  private WikiTable currentTable;
}

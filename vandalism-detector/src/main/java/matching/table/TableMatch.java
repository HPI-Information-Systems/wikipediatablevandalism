package matching.table;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Value;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Represent two matched tables and their respective source revisions according to the matching
 * algorithm.
 */
@Value
@Builder
public class TableMatch {

  private BigInteger previousRevision;
  private WikiTable previousTable;
  private BigInteger currentRevision;
  private WikiTable currentTable;
}

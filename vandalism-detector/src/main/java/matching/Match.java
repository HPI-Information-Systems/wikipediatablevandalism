package matching;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Value;
import wikixmlsplit.renderer.wikitable.WikiTable;

@Value
@Builder
public class Match {

  private BigInteger previousRevision;
  private WikiTable previousTable;
  private BigInteger currentRevision;
  private WikiTable currentTable;
}

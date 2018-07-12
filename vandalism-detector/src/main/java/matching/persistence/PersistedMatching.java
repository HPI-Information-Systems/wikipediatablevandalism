package matching.persistence;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wikixmlsplit.api.Matching;

/**
 * Wrap a matching together with redundant information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class PersistedMatching {

  private BigInteger pageId;
  private int maxRevisionId;
  private Matching matching;
}

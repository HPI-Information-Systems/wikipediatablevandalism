package matching.row;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RowMatch {

  private int matchedIndex;
  private double similarity;
}

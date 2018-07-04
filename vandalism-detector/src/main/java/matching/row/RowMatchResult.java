package matching.row;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class RowMatchResult {

  @Singular
  private final List<RowMatch> matches;

}

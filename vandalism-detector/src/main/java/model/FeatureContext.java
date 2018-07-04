package model;

import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

@Value
@Builder
@RequiredArgsConstructor(staticName = "with")
public class FeatureContext {

  private final MyPageType page;
  private final List<MyRevisionType> previousRevisions;
  private final TableMatchResult result;
  private final TableMatch relevantMatch;
}
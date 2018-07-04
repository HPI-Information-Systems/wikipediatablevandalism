package model;

import lombok.Builder;
import lombok.Value;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

import java.util.List;

@Value
@Builder
public class FeatureContext {

  private final MyPageType page;
  private final List<MyRevisionType> previousRevisions;
  private final TableMatchResult result;
  private final TableMatch relevantMatch;
}
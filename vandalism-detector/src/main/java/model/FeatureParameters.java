package model;

import java.util.List;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Value;
import matching.row.RowMatchResult;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

@Value
@Builder
public class FeatureParameters {

  private MyPageType page;
  private MyRevisionType revision;
  private MyRevisionType previousRevision;
  private List<MyRevisionType> previousRevisions;
  private TableMatchResult result;
  private TableMatch relevantMatch;
  private RowMatchResult rowMatchResult;

  @Nullable
  public MyRevisionType getPreviousRevision() {
    return previousRevision;
  }

  @Nullable
  public TableMatch getRelevantMatch() {
    return relevantMatch;
  }
}
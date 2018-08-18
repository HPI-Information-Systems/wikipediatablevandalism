package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  /**
   * Preprocessed userComment; auto-generated content excluded
   */
  private String userComment;

  /**
   * Original userComment; non-null; may contain auto-generated content
   */
  private String rawComment;

  private String contentWithComment;

  private String content;

  private String previousContent;

  @Nullable
  public MyRevisionType getPreviousRevision() {
    return previousRevision;
  }

  public List<MyRevisionType> getPreviousRevisions() {
    return new ArrayList<>(previousRevisions);
  }

  @Nullable
  public TableMatch getRelevantMatch() {
    return relevantMatch;
  }

  public boolean hasRelevantMatch() {
    return relevantMatch != null;
  }

  public Optional<TableMatch> getMatch() {
    return Optional.ofNullable(relevantMatch);
  }

}
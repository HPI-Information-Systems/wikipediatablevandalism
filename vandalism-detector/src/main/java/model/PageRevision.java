package model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor(staticName = "of")
public class PageRevision {

  private final int pageId;
  private final int revisionId;
}

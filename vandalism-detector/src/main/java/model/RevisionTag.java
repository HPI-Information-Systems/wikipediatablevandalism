package model;

import lombok.Data;

@Data
public class RevisionTag {

  private final PageRevision pageRevision;
  private final Tag tag;
}

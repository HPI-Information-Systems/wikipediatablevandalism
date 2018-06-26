package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevisionTag {
    private final int revisionId;
    private final int revisionPageId;
    private final int tagId;
}

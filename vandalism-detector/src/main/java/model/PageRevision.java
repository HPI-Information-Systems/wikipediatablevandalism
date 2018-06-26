package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageRevision {
    private final int pageId;
    private final int revisionId;
}

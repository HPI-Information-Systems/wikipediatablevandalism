package model;

public class RevisionTag {
    public final int revisionId;
    public final int revisionPageId;
    public final int tagId;

    public RevisionTag(int revisionId, int revisionPageId, int tagId) {
        this.revisionId = revisionId;
        this.revisionPageId = revisionPageId;
        this.tagId = tagId;
    }
}

-- Show tag name with total revision count
SELECT tag.name, count(revision.id)
FROM tag, revision, revisiontag
WHERE tag.id = revisiontag.tag_id AND revision.id = revisiontag.revision_id
GROUP BY tag.name


-- Output count of vandalized revisions
SELECT DISTINCT(revision_id) AS VandalizedRevisionCount
FROM revisiontag, tag
WHERE revisiontag.tag_id = tag.id AND tag.name <> 'constructive';

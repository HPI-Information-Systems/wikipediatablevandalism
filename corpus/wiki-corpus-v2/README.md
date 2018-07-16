# Wikipedia Table Vandalism Corpus
### Schema ###
```
|revision|<--|revisiontag|-->|tag|
```

### Files ###
- `revision.csv` Meta data of all 5161 tagged revisions.
- `revisiontag.csv` All tagged revisions, one row per revision per tag.
- `tag.csv` All used tags with names.
 
### Revision categories ###
- `revisiontag_created_1k.csv` Tags for 1000 randomly selected revisions that contain table creations.
- `revisiontag_deleted_1k.csv` Tags for 1000 randomly selected revisions that contain table deletions.
- `revisiontag_modified_1k.csv` Tags for 1000 randomly selected revisions that contain table modifications.
- `revisiontag_modified_vandalism_filter.csv` Tags for 2161 revisions that contain table modifications and users marked vandalism in one of the upcoming three revisions.
- `revisiontag_modified_total.csv` Tags for all 3161 revisions that contain table modifications, combining `revisiontag_modified_1k.csv` and `revisiontag_modified_vandalism_filter.csv`.

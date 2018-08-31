# Wikipedia Table Vandalism Corpus
This is a corpus of revisions that contain table edits on Wikipedia, tagged if and what kind of vandalism they contain.
Eight tags were defined in total, tags with ids 1-9 denote the kind of vandalism in a revision. The tag intention (id 11) marks if the kind of vandalism was commited on purpose and the constructive tag (id 12) labels non-vandalizing edits.

### Schema ###
```
|revision|<--|revisiontag|-->|tag|
```

### Complete Corpus ###
- `revision.csv` Meta data of all 5161 tagged revisions.
- `revisiontag.csv` All tagged revisions, one row per revision per tag.
- `tag.csv` All used tags with names.
 
### Datasets ###
- `revisiontag_created_1k.csv` Tags for 1000 randomly selected revisions that contain table creations.
- `revisiontag_deleted_1k.csv` Tags for 1000 randomly selected revisions that contain table deletions.
- `revisiontag_modified_1k.csv` Tags for 1000 randomly selected revisions that contain table modifications.
- `revisiontag_modified_vandalism_filter.csv` Tags for 2161 revisions that contain table modifications and users marked vandalism in one of the upcoming three revisions.
- `revisiontag_modified_total.csv` Tags for all 3161 revisions that contain table modifications, combining `revisiontag_modified_1k.csv` and `revisiontag_modified_vandalism_filter.csv`.

### Tag Descriptions ###
| Id | Name         | Description                               |
|----|--------------|-------------------------------------------|
| 1  | Blanking     | Erasing (parts of) an article             |
| 2  | Nonsense     | Content is unrelated; profanity etc.      |
| 3  | QualityIssue | Content is unsourced; unencyclopedic etc. |
| 5  | FalseFact    | Harms truthfulness of the article         |
| 6  | Syntax       | Causes wrong article presentation         |
| 9  | EditWar      | Alternating controversial edits           |
| 11 | Intention    | Intention to damage Wikipedia             |
| 12 | Constructive | Non-vandalizing edits                     |

### Tag Distributions ####
| Id | Name         | Number of revisions |
|----|--------------|---------------------|
| 1  | Blanking     | 499                 |
| 2  | Nonsense     | 459                 |
| 3  | QualityIssue | 257                 |
| 5  | FalseFact    | 446                 |
| 6  | Syntax       | 135                 |
| 9  | EditWar      | 74                  |
| 11 | Intention    | 731                 |
| 12 | Constructive | 3628                |

### Reference ###
- Paper: Vandalism Detection in Wikipedia Tables
- Authors: Falco Duersch, Philipp Hager, Martin Schlegel
Tobias Bleifuß, Leon Bornemann, Dr. Felix Naumann
- University: Hasso-Plattner-Institut, University of Potsdam, Germany
- Date: August 2018

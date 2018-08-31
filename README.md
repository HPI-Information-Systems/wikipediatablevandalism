# Vandalism Detection in Wikipedia Table Revisions

This project contains a vandalism detection system for table edits on Wikipedia. This repository publishes the source code as well as the data used for the paper `Vandalism Detection in Wikipedia Table Revisions` by Dürsch, Hager, Schlegel et al. and was produced in research at the Hasso-Plattner Institute, Unversity of Potsdam, Germany.

## Structure
- `corpus/` A newly created and fine-granular corpus of vandalized Wikipedia table edits.
- `sql/` SQL scripts used for storing an index of all Wikipedia revisions in the process of corpus construction.
- `tagger/` A command line tool for browser-based vandalism tagging on Wikipedia used in corpus annotation.
- `vandalism-classifier/` Jupyter Notebooks to generate and evaluate binary and multilabel vandalism classifiers.
- `vandalism-detector/` Generates features from page revisions used by the `vandalism-classifier`.
- `wiki-revision-index/` Creates an index of page revisions (stored in PostgreSQL database) used for corpus construction.

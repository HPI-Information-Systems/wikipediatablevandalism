-- Export the set of created, modified, deleted revisions
-- Assumptions:
--  -> current shell is in 'aligned' mode (ASCII-art style output)
--    ( \a toggles into CSV compatible mode at the beginning and back after completion)
--  -> The current working directory is writable.

\a
\o created.csv
select * from allCreatedTables;

\o removed.csv
select * from allDeletedTables;

\o modified.csv
select * from allModifiedTables;

\o
\a


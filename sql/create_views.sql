--all tables
CREATE OR REPLACE VIEW changedTables AS SELECT * FROM revision WHERE NOT (table_count = 0 AND changed_tables = 0);

--window of page_id´s
CREATE OR REPLACE VIEW windows AS SELECT *, rank() OVER (PARTITION BY page_id ORDER BY created_at) FROM changedTables;

--all deleted
CREATE OR REPLACE VIEW allDeletedTables
AS SELECT DISTINCT actual.id, actual.page_id, actual.created_at, actual.table_count, actual.changed_tables, before.table_count AS before_table_count
FROM windows actual
INNER JOIN windows before
ON actual.page_id = before.page_id AND before.rank+1 = actual.rank AND actual.table_count < before.table_count;

--all created
CREATE OR REPLACE VIEW allCreatedTables
AS SELECT DISTINCT actual.id, actual.page_id, actual.created_at, actual.table_count, actual.changed_tables, before.table_count AS table_count_before
FROM windows actual
INNER JOIN windows before
ON actual.page_id = before.page_id AND before.rank+1 = actual.rank AND actual.table_count > before.table_count;

--all modified
CREATE OR REPLACE VIEW allModifiedTables
AS SELECT DISTINCT actual.id, actual.page_id, actual.created_at, actual.table_count, actual.changed_tables, before.table_count AS table_count_before
FROM windows actual
INNER JOIN windows before
ON actual.page_id = before.page_id AND before.rank+1 = actual.rank AND actual.table_count = before.table_count AND actual.changed_tables > 0;

--finalDeletedTables
CREATE OR REPLACE VIEW deletedTables AS SELECT * FROM allDeletedTables ORDER BY RANDOM() LIMIT 1000;

--finalCreatedTables
CREATE OR REPLACE VIEW createdTables AS SELECT * FROM allCreatedTables ORDER BY RANDOM() LIMIT 1000;

--finalModifiedTables
CREATE OR REPLACE VIEW modifiedTables AS SELECT * FROM allModifiedTables ORDER BY RANDOM() LIMIT 1000;

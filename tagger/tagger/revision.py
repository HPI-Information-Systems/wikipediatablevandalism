#! -*- encoding: utf-8 -*-
from collections import namedtuple


Revision = namedtuple('Revision', ('id', 'page_id', 'created_at', 'has_tables', 'table_hash'))


class RevisionController(object):

    def __init__(self, connection, open_handler, tag_controller):
        self.connection = connection
        self.tag_controller = tag_controller
        self.open_handler = open_handler
        self.cursor = None
        self.current_revision = None

    def setup(self):
        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("""
            CREATE TABLE IF NOT EXISTS RevisionTag (revision_id SERIAL, revision_page_id SERIAL, tag_id SERIAL,
            FOREIGN KEY (revision_id, revision_page_id) REFERENCES Revision(id, page_id),
            FOREIGN KEY (tag_id) REFERENCES Tag(id),
            UNIQUE (revision_id, revision_page_id, tag_id))
            """)

        self.cursor = self.connection.cursor()
        self.cursor.execute("""
        SELECT id, page_id, created_at, has_tables, table_hash FROM Revision WHERE has_tables IS TRUE
        """)

    def get_next_revision(self):
        self._next()
        return self.current_revision

    def mark_current_revision(self, tags):
        if not self.current_revision:
            return

        tag_ids = self.tag_controller.find_tag_ids(tags)
        to_insert = [(self.current_revision.id, self.current_revision.page_id, tag_id) for tag_id in tag_ids]
        with self.connection, self.connection.cursor() as cursor:
            cursor.executemany("""
            INSERT INTO RevisionTag(revision_id, revision_page_id, tag_id) VALUES (%s, %s, %s) ON CONFLICT DO NOTHING
            """, to_insert)

    def get_current_revision(self):
        if not self.current_revision:
            self._next()
        return self.current_revision

    def open_current_revision(self):
        self.open_handler.open(self.get_current_revision().id)

    def _next(self):
        next_item = next(self.cursor, None)

        if not next_item:
            self.current_revision = None
            return

        rev_id, page_id, created_at, has_tables, table_hash = next_item
        self.current_revision = Revision(id=rev_id,
                                         page_id=page_id,
                                         created_at=created_at,
                                         has_tables=has_tables,
                                         table_hash=table_hash)

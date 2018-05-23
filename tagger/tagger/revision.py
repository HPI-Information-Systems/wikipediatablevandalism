#! -*- encoding: utf-8 -*-
from collections import namedtuple
import logging
from .tag import TAG_CONSTRUCTIVE

Revision = namedtuple('Revision', ('id', 'page_id', 'created_at', 'table_count', 'changed_tables'))


class FileRevisionSource(object):

    def __init__(self, filename):
        self.filename = filename
        with open(self.filename, 'r') as f:
            self.revisions = list(filter(bool, [line.strip() for line in f]))
        self.iterator = iter(self.revisions)

    def next_revision(self):
        return next(self.iterator, None)


class RevisionController(object):

    def __init__(self, connection, open_handler, tag_controller, revision_source):
        self.connection = connection
        self.tag_controller = tag_controller
        self.open_handler = open_handler
        self.revision_source = revision_source
        self.current_revision = None
        self.logger = logging.getLogger(__name__)

    def setup(self):
        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("""
            CREATE TABLE IF NOT EXISTS RevisionTag (revision_id SERIAL, revision_page_id SERIAL, tag_id SERIAL,
            FOREIGN KEY (revision_id, revision_page_id) REFERENCES Revision(id, page_id),
            FOREIGN KEY (tag_id) REFERENCES Tag(id),
            UNIQUE (revision_id, revision_page_id, tag_id))
            """)

    def get_next_revision(self):
        while True:
            revision_id = self.revision_source.next_revision()
            if revision_id:
                revision = self._fetch_revision(revision_id)
                if revision:
                    self.current_revision = revision
                    return revision
                else:
                    self.logger.warning("Revision ID %s not found, trying next", revision_id)
                    continue
            else:
                self.logger.debug("Revision source exhausted")
                self.current_revision = None
                break

    def mark_current_revision(self, tags):
        if not self.current_revision:
            return

        if not tags:
            tags = [TAG_CONSTRUCTIVE]

        tag_ids = self.tag_controller.find_tag_ids(tags)
        to_insert = [(self.current_revision.id, self.current_revision.page_id, tag_id) for tag_id in tag_ids]
        with self.connection, self.connection.cursor() as cursor:
            cursor.executemany("""
            INSERT INTO RevisionTag(revision_id, revision_page_id, tag_id) VALUES (%s, %s, %s) ON CONFLICT DO NOTHING
            """, to_insert)

        self.logger.info("Marked revision %s with %s", self.current_revision.id, tags)

    def get_current_revision(self):
        if not self.current_revision:
            self.get_next_revision()
        return self.current_revision

    def open_current_revision(self):
        self.open_handler.open(self.get_current_revision().id)

    def _fetch_revision(self, revision_id):
        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("SELECT id, page_id, created_at, table_count, changed_tables FROM Revision WHERE id = %s",
                           (revision_id,))
            record = cursor.fetchone()
            if record:
                rev_id, page_id, created_at, table_count, changed_tables = record
                return Revision(id=rev_id,
                                page_id=page_id,
                                created_at=created_at,
                                table_count=table_count,
                                changed_tables=changed_tables)

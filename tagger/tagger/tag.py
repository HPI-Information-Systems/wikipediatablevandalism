#! -*- encoding: utf-8 -*-
import json
import logging


TAG_CONSTRUCTIVE = 'constructive'


class Tag(object):
    def __init__(self, pk, name, visible=True, enabled=True):
        self.pk = pk
        self.name = name
        self.visible = visible
        self.enabled = enabled

    def __repr__(self):
        return self.name


def read_tags(filename):
    with open(filename, 'r') as f:
        tags = [Tag(**t) for t in json.load(f)]
        return [t for t in tags if t.enabled]


class DatabaseTagController(object):

    def __init__(self, connection, tags):
        self.connection = connection
        self.tags = tags
        self.logger = logging.getLogger(__name__)

    def setup(self):
        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("CREATE TABLE IF NOT EXISTS Tag (id SERIAL PRIMARY KEY, name varchar(30) UNIQUE)")
            to_insert = [(t.pk, t.name) for t in self.tags]
            cursor.executemany("""INSERT INTO Tag(id, name) VALUES (%s, %s) ON CONFLICT DO NOTHING""", to_insert)

        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("""
            CREATE TABLE IF NOT EXISTS RevisionTag (revision_id SERIAL, revision_page_id SERIAL, tag_id SERIAL,
            FOREIGN KEY (revision_id, revision_page_id) REFERENCES Revision(id, page_id),
            FOREIGN KEY (tag_id) REFERENCES Tag(id),
            UNIQUE (revision_id, revision_page_id, tag_id))
            """)

    def mark_revision(self, page_id, revision_id, tags):
        tag_ids = [t.pk for t in tags]
        to_insert = [(revision_id, page_id, tag_id) for tag_id in tag_ids]
        with self.connection, self.connection.cursor() as cursor:
            cursor.executemany("""
                            INSERT INTO RevisionTag(revision_id, revision_page_id, tag_id) VALUES (%s, %s, %s) ON CONFLICT DO NOTHING
                            """, to_insert)

    def get_tags(self):
        return [t for t in self.tags if t.visible]


class FileTagController(object):

    def __init__(self, filename, tags):
        self.filename = filename
        self.tags = tags
        self.logger = logging.getLogger(__name__)

    def mark_revision(self, page_id, revision_id, tags):
        tag_ids = [t.pk for t in tags]
        lines = self._get_lines(page_id, revision_id, tag_ids)
        with open(self.filename, 'a') as f:
            for line in lines:
                f.write(line + '\n')

    def _get_lines(self, page_id, revision_id, tag_ids):
        records = [(revision_id, page_id, str(tag_id)) for tag_id in tag_ids]
        return [";".join(record) for record in records]

    def get_tags(self):
        return [t for t in self.tags if t.visible]

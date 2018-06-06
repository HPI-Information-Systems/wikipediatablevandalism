#! -*- encoding: utf-8 -*-

import logging

TAG_CONSTRUCTIVE = 'constructive'


def read_tags(filename):
    with open(filename, 'r') as f:
        lines = [line.strip() for line in f]
        return [line for line in lines if line]


class DatabaseTagController(object):

    def __init__(self, connection, tags):
        self.connection = connection
        self.tags = tags
        self.logger = logging.getLogger(__name__)

    def setup(self):
        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("CREATE TABLE IF NOT EXISTS Tag (id SERIAL PRIMARY KEY, name varchar(30) UNIQUE)")
            all_tags = self.tags + [TAG_CONSTRUCTIVE]
            cursor.executemany("""INSERT INTO Tag(name) VALUES (%s) ON CONFLICT DO NOTHING""",
                               [(t,) for t in all_tags])

        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("""
            CREATE TABLE IF NOT EXISTS RevisionTag (revision_id SERIAL, revision_page_id SERIAL, tag_id SERIAL,
            FOREIGN KEY (revision_id, revision_page_id) REFERENCES Revision(id, page_id),
            FOREIGN KEY (tag_id) REFERENCES Tag(id),
            UNIQUE (revision_id, revision_page_id, tag_id))
            """)

    def find_tag_ids(self, tags):
        tag_ids = []
        with self.connection, self.connection.cursor() as cursor:
            for tag in tags:
                cursor.execute("SELECT id FROM Tag WHERE name = %s LIMIT 1", (tag,))
                record = cursor.fetchone()
                if record:
                    tag_id, = record
                    tag_ids.append(tag_id)
                else:
                    self.logger.warning("Tag not found: %s", tag)
        return tag_ids

    def mark_revision(self, page_id, revision_id, tags):
        tag_ids = self.find_tag_ids(tags)
        to_insert = [(revision_id, page_id, tag_id) for tag_id in tag_ids]
        with self.connection, self.connection.cursor() as cursor:
            cursor.executemany("""
                            INSERT INTO RevisionTag(revision_id, revision_page_id, tag_id) VALUES (%s, %s, %s) ON CONFLICT DO NOTHING
                            """, to_insert)

    def get_tags(self):
        return self.tags


class FileTagController(object):

    def __init__(self, filename, tags):
        self.filename = filename
        self.tags = tags
        self.all_tags = self.tags + [TAG_CONSTRUCTIVE]
        self.logger = logging.getLogger(__name__)

    def mark_revision(self, page_id, revision_id, tags):
        if not tags:
            tags = [TAG_CONSTRUCTIVE]
        tag_ids = self.to_tag_ids(tags)
        lines = self._get_lines(page_id, revision_id, tag_ids)
        with open(self.filename, 'a') as f:
            for line in lines:
                f.write(line + '\n')

    def to_tag_ids(self, tags):
        ids = [self.all_tags.index(tag) + 1 for tag in tags]
        assert all(x > 0 for x in ids), "All tags should be found"
        return ids

    def _get_lines(self, page_id, revision_id, tag_ids):
        records = [(revision_id, page_id, str(tag_id)) for tag_id in tag_ids]
        return [";".join(record) for record in records]

    def get_tags(self):
        return self.tags

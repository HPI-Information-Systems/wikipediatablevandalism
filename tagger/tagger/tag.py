#! -*- encoding: utf-8 -*-

import logging


class TagController(object):

    def __init__(self, filename, connection):
        self.filename = filename
        self.connection = connection
        self.logger = logging.getLogger(__name__)

    def setup(self):
        with self.connection, self.connection.cursor() as cursor:
            cursor.execute("CREATE TABLE IF NOT EXISTS Tag (id SERIAL PRIMARY KEY, name varchar(30) UNIQUE)")
            tags = self.get_tags()
            cursor.executemany("""INSERT INTO Tag(name) VALUES (%s) ON CONFLICT DO NOTHING""",
                               [(t,) for t in tags])

    def get_tags(self):
        with open(self.filename, 'r') as f:
            lines = [line.strip() for line in f]
            return [line for line in lines if line]

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

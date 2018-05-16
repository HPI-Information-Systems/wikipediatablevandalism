#! -*- encoding: utf-8 -*-


class TagController(object):

    def __init__(self, filename, connection):
        self.filename = filename
        self.connection = connection

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
                tag_id, = cursor.fetchone()
                assert tag_id, "Tag not found in DB"
                tag_ids.append(tag_id)
        return tag_ids

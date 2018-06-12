#! -*- encoding: utf-8 -*-

import argparse
import logging
import os

from tagger.tag import FileTagController, DatabaseTagController, read_tags
from tagger.revision import RevisionController, FileRevisionSource
from tagger.open_handler import OpenHandler


def parse_args():
    parser = argparse.ArgumentParser(description="Tag Wikipedia Revisions",
                                     formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument('--no-selenium', action='store_true',
                        help='Use Selenium Web Driver to remotely control the browser')
    parser.add_argument('-t', '--tags', default='tags.txt',
                        help='Text file with tags, one per line')
    parser.add_argument('-r', '--revisions', default='revisions.txt',
                        help='Text file with revisions to tag, one per line')
    parser.add_argument('--revision-delimiter', default='|',
                        help='Delimiter of the revsions file')
    parser.add_argument('-l', '--log',
                        help='Filename of the logfile')
    parser.add_argument('-s', '--last-seen',
                        help='Resume a tagging session by specifying the last seen revision ID')
    parser.add_argument('--output', '-o',
                        help='Where to store tags; if omitted, the database is used')
    parser.add_argument('--no-curses', action='store_true', default=True,
                        help='Disable Curses UI; implicitly activated if on Windows')
    parser.add_argument('--use-database', action='store_true',
                        help='Enable Postgres access')

    pg = parser.add_argument_group('Postgres', description='Database settings')
    pg.add_argument('--host', help='Host', default='localhost')
    pg.add_argument('--database', help='Database', default='vandalism')
    pg.add_argument('--username', help='Username')
    pg.add_argument('--password', help='Password')

    return parser.parse_args()


def create_open_handler(args):
    if args.no_selenium:
        return OpenHandler.fallback()
    return OpenHandler.selenium()


def create_database_tag_controller(connection, tags):
    tc = DatabaseTagController(connection=connection, tags=tags)
    tc.setup()
    return tc


def create_revision_controller(open_handler, tag_controller, revisions):
    return RevisionController(open_handler, tag_controller, revisions)


def create_connection(args):
    import psycopg2
    return psycopg2.connect(host=args.host,
                            database=args.database,
                            username=args.username,
                            password=args.password)


def create_revision_source(args):
    return FileRevisionSource(args.revisions, args.revision_delimiter, args.last_seen)


def setup_logging(args):
    filename = args.log or replace_extension(args.revisions, ".log")
    logging.basicConfig(filename=filename,
                        level=logging.INFO,
                        format='%(asctime)s %(name)-14s %(levelname)-8s %(message)s')


def replace_extension(filename, new_extension):
    name, _ = filename.rsplit(".", 1)
    return name + new_extension


def main():
    args = parse_args()
    setup_logging(args)
    revisions = create_revision_source(args)
    tags = read_tags(args.tags)

    if args.use_database:
        connection = create_connection(args)
        tag_controller = create_database_tag_controller(connection, tags)
    else:
        connection = None
        output_file = args.output or replace_extension(args.revisions, "_tags.csv")
        tag_controller = FileTagController(output_file, tags)

    try:
        with create_open_handler(args) as open_handler:
            revision_controller = create_revision_controller(open_handler, tag_controller, revisions)
            if os.name == 'nt' or args.no_curses:
                from tagger.frontend_mini import MinimalFrontend
                MinimalFrontend.main(tag_controller, revision_controller)
            else:
                from tagger.frontend_curses import CursesFrontend
                CursesFrontend.main(tag_controller, revision_controller)
    finally:
        if connection:
            connection.close()


if __name__ == '__main__':
    main()

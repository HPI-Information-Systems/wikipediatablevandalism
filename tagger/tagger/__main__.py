#! -*- encoding: utf-8 -*-

import argparse
import logging
import psycopg2

from tagger.frontend import CursesFrontend
from tagger.tag import TagController
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
    parser.add_argument('-l', '--log', default='log.txt',
                        help='Filename of the logfile')
    parser.add_argument('-s', '--last-seen',
                        help='Resume a tagging session by specifying the last seen revision ID')

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


def create_tag_controller(connection, args):
    tc = TagController(filename=args.tags,
                       connection=connection)
    tc.setup()
    return tc


def create_revision_controller(connection, open_handler, tag_controller, revisions):
    rc = RevisionController(connection, open_handler, tag_controller, revisions)
    rc.setup()
    return rc


def create_connection(args):
    return psycopg2.connect(host=args.host,
                            database=args.database,
                            username=args.username,
                            password=args.password)


def create_revision_source(args):
    return FileRevisionSource(args.revisions, args.last_seen)


def setup_logging(args):
    logging.basicConfig(filename=args.log,
                        level=logging.INFO,
                        format='%(asctime)s %(name)-14s %(levelname)-8s %(message)s')


def main():
    args = parse_args()
    setup_logging(args)
    revisions = create_revision_source(args)
    connection = create_connection(args)
    try:
        tag_controller = create_tag_controller(connection, args)
        with create_open_handler(args) as open_handler:
            revision_controller = create_revision_controller(connection, open_handler, tag_controller, revisions)
            CursesFrontend.main(tag_controller, revision_controller)
    finally:
        connection.close()


if __name__ == '__main__':
    main()

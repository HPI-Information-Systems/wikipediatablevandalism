#! -*- encoding: utf-8 -*-
from collections import namedtuple
import logging

Revision = namedtuple('Revision', ('id', 'page_id'))


class InvalidRevisionId(BaseException):
    pass


class FileRevisionSource(object):

    def __init__(self, filename, sep, start_revision=None):
        self.filename = filename
        self.sep = sep
        revisions = self._get_revisions(start_revision)
        self.count = len(revisions)
        self.iterator = iter(revisions)

    def _get_revisions(self, start_revision_id):
        revisions = self._read_file()
        revision_ids = [r.id for r in revisions]

        if start_revision_id is None:
            return revisions

        if start_revision_id not in revision_ids:
            raise InvalidRevisionId("Start revision %s not found" % start_revision_id)

        begin = revision_ids.index(start_revision_id) + 1
        return revisions[begin:]

    def _read_file(self):
        with open(self.filename, 'r') as f:
            revisions = []
            for line in f:
                line = line.strip()
                if not line:
                    continue
                page_id, revision_id = line.split(self.sep)[:2]
                revisions.append(Revision(page_id, revision_id))
            return revisions

    def next_revision(self):
        return next(self.iterator, None)

    def revision_count(self):
        return self.count


class RevisionController(object):

    def __init__(self, open_handler, tag_controller, revision_source):
        self.tag_controller = tag_controller
        self.open_handler = open_handler
        self.revision_source = revision_source
        self.current_revision = None
        self.seen = 0
        self.logger = logging.getLogger(__name__)

    def get_next_revision(self):
        self.current_revision = self.revision_source.next_revision()
        return self.current_revision

    def mark_current_revision(self, tags):
        if not self.current_revision:
            return

        if not tags:
            tags = self.tag_controller.fallback_tags()

        self.tag_controller.mark_revision(self.current_revision.page_id,
                                          self.current_revision.id,
                                          tags)
        self.logger.info("Marked revision %s with %s", self.current_revision.id, tags)
        self.seen += 1

    def get_current_revision(self):
        if not self.current_revision:
            self.get_next_revision()
        return self.current_revision

    def open_current_revision(self):
        self.open_handler.open(self.get_current_revision().id)

    def get_progress(self):
        return self.seen, self.revision_source.revision_count()

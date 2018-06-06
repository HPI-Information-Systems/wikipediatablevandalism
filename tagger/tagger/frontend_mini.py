#! -*- encoding: utf-8 -*-
import sys


class MinimalFrontend(object):

    def __init__(self, tag_controller, revision_controller):
        self.tag_controller = tag_controller
        self.revision_controller = revision_controller

    def run(self):
        while True:
            revision = self.revision_controller.get_next_revision()
            if not revision:
                break

            self.revision_controller.open_current_revision()
            self.print_revision(revision)
            self.print_tags()
            selected_tags = self.read_input()
            if selected_tags is None:
                break

            self.revision_controller.mark_current_revision(selected_tags)
            print("\n" + ("=" * 15) + "\n")

    def print_revision(self, revision):
        seen, total = self.revision_controller.get_progress()
        print("Revision {} from page {} ({} remaining)".format(revision.id, revision.page_id, total - seen))

    def print_tags(self):
        for index, tag in enumerate(self.tag_controller.get_tags()):
            print("({}) {}".format(index + 1, tag))

    def read_input(self):
        tags = self.tag_controller.get_tags()
        while True:
            try:
                read = _read("Tag Numbers: ")

                if not len(read):
                    return []

                if "quit" in read or "q" in read:
                    return

                selected_ids = read.split(" ")
                return [tags[int(x) - 1] for x in selected_ids]
            except ValueError:
                print("Input not recognized, please try again.")
            except KeyboardInterrupt:
                print("Quitting...")
                return

    @classmethod
    def main(cls, tag_controller, revision_controller):
        MinimalFrontend(tag_controller, revision_controller).run()


def _read(prompt):
    is_python3 = sys.version_info[0] == 3
    if is_python3:
        return input(prompt)
    else:
        return raw_input(prompt)

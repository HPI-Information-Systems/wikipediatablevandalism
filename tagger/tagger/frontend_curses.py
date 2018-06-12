#! -*- encoding: utf-8 -*-

import curses
import curses.ascii


class CursesFrontend(object):
    TAG_HEIGHT = 1

    def __init__(self, tag_controller, revision_controller):
        self.tag_controller = tag_controller
        self.revision_controller = revision_controller
        self.highlight = 0
        self.actions = {
            curses.KEY_UP: self._previous_tag,
            curses.KEY_DOWN: self._next_tag,
            ord('w'): self._remove_tag,
            ord('j'): self._show_next_revision,
            ord('q'): self._quit
        }
        self.selected_tags = set()

    def _initialize(self):
        curses.noecho()
        curses.cbreak()

        self.tags = self.tag_controller.get_tags()
        if not self.revision_controller.get_next_revision():
            raise QuitException("Could not retrieve first revision")

    def _quit(self):
        raise QuitException

    def _draw(self):
        window = self._create_root_window()

        for index, tag in enumerate(self.tags):
            self._draw_tag(window, index, tag)

        window.refresh()

    def _create_root_window(self):
        window = curses.newwin(0, 0, 0, 0)
        window.addstr(1, 1, "Wikipedia Table Revisions Analyzer")

        revision = self.revision_controller.get_current_revision()
        window.addstr(2, 1, "Current Revision: {}, page_id={}".format(revision.id, revision.page_id))

        seen, total = self.revision_controller.get_progress()
        window.addstr(3, 1, "Progress: {} remaining".format(total - seen))

        selected_tags = ", ".join(t.name for t in self.selected_tags)
        window.addstr(4, 1, "Current Tags: {}".format(selected_tags))
        return window

    def _draw_tag(self, window, index, tag):
        sub = window.subwin(
            # number of preceding lines from _create_root_window + 1 + box count times height
            5 + index * CursesFrontend.TAG_HEIGHT,
            1)
        style = curses.A_STANDOUT if self.highlight == index else curses.A_NORMAL
        message = "({}) {}".format(index + 1, tag)
        self._try_addstr(sub, 1, 1, message, style)

    @classmethod
    def _try_addstr(cls, sub, *args):
        try:
            sub.addstr(*args)
        except curses.error:
            raise InvalidScreenSizeException

    def run(self, screen):
        self._initialize()
        screen.refresh()
        self._draw()
        self.revision_controller.open_current_revision()

        while True:
            typed = screen.getch()
            try:
                self._handle_keypress(typed)
            except QuitException:
                break

            self._draw()
            screen.refresh()

    def _handle_keypress(self, typed):
        if curses.ascii.isdigit(typed):
            tag_nr = typed - ord('0') - 1
            self._select_tag(tag_nr)
        elif typed == curses.KEY_ENTER or typed == 10:
            self._select_tag(self.highlight)
        else:
            self._handle_action(typed)

    def _handle_action(self, typed):
        if typed in self.actions:
            self.actions[typed]()

    def _previous_tag(self):
        self.highlight = max(0, self.highlight - 1)

    def _next_tag(self):
        self.highlight = min(len(self.tags) - 1, self.highlight + 1)

    def _select_tag(self, tag_nr):
        if 0 <= tag_nr < len(self.tags):
            tag = self.tags[tag_nr]
            self.selected_tags.add(tag)
            self.highlight = tag_nr

    def _remove_tag(self):
        to_remove = self.tags[self.highlight]
        if to_remove in self.selected_tags:
            self.selected_tags.remove(to_remove)

    def _show_next_revision(self):
        self.revision_controller.mark_current_revision(self.selected_tags)
        self.selected_tags.clear()
        self.highlight = 0
        if self.revision_controller.get_next_revision():
            self.revision_controller.open_current_revision()
        else:
            self._quit()

    @classmethod
    def main(cls, tag_controller, revision_controller):
        frontend = CursesFrontend(tag_controller, revision_controller)
        curses.wrapper(frontend.run)


class QuitException(BaseException):
    pass


class InvalidScreenSizeException(BaseException):
    """
    Possibly the size of your terminal is too small to display all content.
    Please try to resize it.
    """

    def __init__(self):
        super(InvalidScreenSizeException, self).__init__(self.__doc__)

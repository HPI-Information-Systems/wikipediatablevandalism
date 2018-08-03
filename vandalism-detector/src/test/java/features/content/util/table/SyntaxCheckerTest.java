package features.content.util.table;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.Test;

class SyntaxCheckerTest {

  @Test
  void findWikiOpen() {
    val matcher = SyntaxChecker.TABLE_OPEN_WIKI.matcher("{|");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findWikiClose() {
    val matcher = SyntaxChecker.TABLE_CLOSE_WIKI.matcher("|}");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHTMLOpen() {
    val matcher = SyntaxChecker.TABLE_OPEN_HTML.matcher("<table style=\"width:100%\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHTMLClose() {
    val matcher = SyntaxChecker.TABLE_CLOSE_HTML.matcher("</table>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefOpen() {
    val matcher = SyntaxChecker.REF_OPEN.matcher("<ref name=\"Chomsky\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefClose() {
    val matcher = SyntaxChecker.REF_CLOSE.matcher("</ref>");
    assertThat(matcher.find()).isTrue();
  }

}

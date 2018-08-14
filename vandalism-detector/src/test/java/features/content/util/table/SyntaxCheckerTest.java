package features.content.util.table;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.WordListUtil;

class SyntaxCheckerTest {

  final String filename = "syntax_test.txt";
  private String testContent = "";

  @BeforeEach
  void fillTestContent() {


    val url = WordListUtil.class.getClassLoader().getResource(filename);

    if (url == null) {
      throw new IllegalArgumentException(filename + " does not exist");
    }

    try {
      final Set<String> lines = new HashSet<>(Resources.readLines(url, StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();

      for (val line : lines) {
        sb.append(line);
        sb.append(System.lineSeparator());
      }
      testContent = sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void findBold() {
    val matcher = SyntaxChecker.BOLD.matcher("''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findItalic() {
    val matcher = SyntaxChecker.ITALIC.matcher("'''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBoldItalic() {
    val matcher = SyntaxChecker.BOLD_ITALIC.matcher("'''''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefHTMLOpen() {
    val matcher = SyntaxChecker.REF_HTML_OPEN.matcher("<ref name=\"Chomsky\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefHTMLClose() {
    val matcher = SyntaxChecker.REF_HTML_CLOSE.matcher("</ref>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findLinkInternOpen() {
    val matcher = SyntaxChecker.LINK_EXTERN_OPEN.matcher("[[");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findLinkInternClose() {
    val matcher = SyntaxChecker.LINK_EXTERN_CLOSE.matcher("]]");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefWikiOpen() {
    val matcher = SyntaxChecker.LINK_EXTERN_OPEN.matcher("[[ref:");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefLinkClose() {
    val matcher = SyntaxChecker.LINK_EXTERN_CLOSE.matcher("]]");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findLinkExternOpen() {
    val matcher = SyntaxChecker.LINK_EXTERN_OPEN.matcher("[");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findLinkExternClose() {
    val matcher = SyntaxChecker.LINK_EXTERN_CLOSE.matcher("]");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCurlyBracesOpen() {
    val matcher = SyntaxChecker.CURLY_BRACES_OPEN.matcher("{");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCurlyBracesClose() {
    val matcher = SyntaxChecker.CURLY_BRACES_CLOSE.matcher("}");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMathOpen() {
    val matcher = SyntaxChecker.MATH_FORMULA_OPEN.matcher("<math style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMathClose() {
    val matcher = SyntaxChecker.MATH_FORMULA_CLOSE.matcher("</math>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSpanOpen() {
    val matcher = SyntaxChecker.SPAN_OPEN.matcher("<span style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSpanClose() {
    val matcher = SyntaxChecker.SPAN_CLOSE.matcher("</span>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSyntaxhighlightOpen() {
    val matcher = SyntaxChecker.SYNTAXHIGHLIGHT_OPEN.matcher("<syntaxhighlight style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSyntaxhighlightClose() {
    val matcher = SyntaxChecker.SYNTAXHIGHLIGHT_CLOSE.matcher("</syntaxhighlight>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSmallOpen() {
    val matcher = SyntaxChecker.SMALL_OPEN.matcher("<small style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSmallClose() {
    val matcher = SyntaxChecker.SMALL_CLOSE.matcher("</small>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBigOpen() {
    val matcher = SyntaxChecker.BIG_OPEN.matcher("<big style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBigClose() {
    val matcher = SyntaxChecker.BIG_CLOSE.matcher("</big>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDivOpen() {
    val matcher = SyntaxChecker.DIV_OPEN.matcher("<div style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDivClose() {
    val matcher = SyntaxChecker.DIV_CLOSE.matcher("</div>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPoemOpen() {
    val matcher = SyntaxChecker.POEM_OPEN.matcher("<poem style=\"cool\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPoemClose() {
    val matcher = SyntaxChecker.POEM_CLOSE.matcher("</poem>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPreOpen() {
    val matcher = SyntaxChecker.PRE_OPEN.matcher("<pre>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPreClose() {
    val matcher = SyntaxChecker.PRE_CLOSE.matcher("</pre>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSuperscriptOpen() {
    val matcher = SyntaxChecker.SUPERSCRIPT_OPEN.matcher("<sup>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSuperscriptClose() {
    val matcher = SyntaxChecker.SUPERSCRIPT_CLOSE.matcher("</sup>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSOpen() {
    val matcher = SyntaxChecker.S_OPEN.matcher("<s>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSClose() {
    val matcher = SyntaxChecker.S_CLOSE.matcher("</s>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findUOpen() {
    val matcher = SyntaxChecker.U_OPEN.matcher("<u>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findUClose() {
    val matcher = SyntaxChecker.U_CLOSE.matcher("</u>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDelOpen() {
    val matcher = SyntaxChecker.DEL_OPEN.matcher("<del>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDelClose() {
    val matcher = SyntaxChecker.DEL_CLOSE.matcher("</del>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findInsOpen() {
    val matcher = SyntaxChecker.INS_OPEN.matcher("<ins>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findInsClose() {
    val matcher = SyntaxChecker.INS_CLOSE.matcher("</ins>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubscriptOpen() {
    val matcher = SyntaxChecker.SUBSCRIPT_OPEN.matcher("<sub>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubscriptClose() {
    val matcher = SyntaxChecker.SUBSCRIPT_CLOSE.matcher("</sub>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCodeOpen() {
    val matcher = SyntaxChecker.CODE_OPEN.matcher("<code>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCodeClose() {
    val matcher = SyntaxChecker.CODE_CLOSE.matcher("</code>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBlockquoteOpen() {
    val matcher = SyntaxChecker.BLOCKQUOTE_OPEN.matcher("<blockquote>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBlockquotelose() {
    val matcher = SyntaxChecker.BLOCKQUOTE_CLOSE.matcher("</blockquote>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCommentOpen() {
    val matcher = SyntaxChecker.COMMENT_OPEN.matcher("<!--");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCommentClose() {
    val matcher = SyntaxChecker.COMMENT_CLOSE.matcher("-->");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findNowikiOpen() {
    val matcher = SyntaxChecker.NOWIKI_OPEN.matcher("<nowiki>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findNowikiClose() {
    val matcher = SyntaxChecker.NOWIKI_CLOSE.matcher("</nowiki>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDlOpen() {
    val matcher = SyntaxChecker.DL_OPEN.matcher("<dl>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDlClose() {
    val matcher = SyntaxChecker.DL_CLOSE.matcher("</dl>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDtOpen() {
    val matcher = SyntaxChecker.DT_OPEN.matcher("<dt>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDtClose() {
    val matcher = SyntaxChecker.DT_CLOSE.matcher("</dt>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDdOpen() {
    val matcher = SyntaxChecker.DD_OPEN.matcher("<dd>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDdClose() {
    val matcher = SyntaxChecker.DD_CLOSE.matcher("</dd>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findScoreOpen() {
    val matcher = SyntaxChecker.SCORE_OPEN.matcher("<score>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findScoreClose() {
    val matcher = SyntaxChecker.SCORE_CLOSE.matcher("</score>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHeading2() {
    val matcher = SyntaxChecker.HEADING2.matcher("==");
    assertThat(matcher.find()).isTrue();
  }


  @Test
  void findHeading3() {
    val matcher = SyntaxChecker.HEADING3.matcher("===");
    assertThat(matcher.find()).isTrue();
  }


  @Test
  void findHeading4() {
    val matcher = SyntaxChecker.HEADING4.matcher("====");
    assertThat(matcher.find()).isTrue();
  }


  @Test
  void findHeading5() {
    val matcher = SyntaxChecker.HEADING5.matcher("=====");
    assertThat(matcher.find()).isTrue();
  }


  @Test
  void findHeading6() {
    val matcher = SyntaxChecker.HEADING6.matcher("======");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void testCheckOpenAndCloseSyntaxCount() {
    val clipCount = SyntaxChecker.getOpenAndCloseSyntaxCount(testContent);
    assertThat(clipCount).isEqualTo(0);
  }

}

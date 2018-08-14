package features.content.util.language.regex;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;

class WikiSyntaxPatternListTest {

  @Test
  void findItalicText() {
    val matcher = WikiSyntaxPatternList.ITALIC.matcher("'' this is italic''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBoldText() {
    val matcher = WikiSyntaxPatternList.BOLD.matcher("'''this is bold'''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBoldAndItalicText() {
    val matcher = WikiSyntaxPatternList.BOLD_ITALIC.matcher("'''''this is bold and italic'''''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMathFormula() {
    val matcher = WikiSyntaxPatternList.MATH_FORMULA.matcher("<math>L^2([a,b])</math>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMathFormulaWithAttributes() {
    val matcher = WikiSyntaxPatternList.MATH_FORMULA
        .matcher("<math display=\"inline\">L^2([a,b])</math>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findUnclosedMathFormula() {
    val matcher = WikiSyntaxPatternList.MATH_FORMULA
        .matcher("<math display=\"inline\">L^2([a,b])<math>");
    assertThat(matcher.find()).isFalse();
  }

  @Test
  void findSuperscriptText() {
    val matcher = WikiSyntaxPatternList.SUPERSCRIPT.matcher("''L''<sup>2</sup>([''a'',''b''])");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubscriptText() {
    val matcher = WikiSyntaxPatternList.SUBSCRIPT.matcher("Text is<sub>subscript</sub>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPoemText() {
    val matcher = WikiSyntaxPatternList.POEM.matcher("<poem>"
        + "Roses are grey"
        + "Violets are a different shade of grey"
        + "Let's go chase cars."
        + "That's a poem by a dog"
        + "</poem>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCodeText() {
    val matcher = WikiSyntaxPatternList.CODE.matcher("<code>"
        + "public void thisIsCode() {"
        + "}"
        + "</code>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findNonBreakingSpace() {
    val matcher = WikiSyntaxPatternList.NON_BREAKING_SPACE
        .matcher("Nr.&nbsp;1, 10&nbsp;kg, Dr.&nbsp;Best");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findComment() {
    val matcher = WikiSyntaxPatternList.COMMENT.matcher("This is a <!--invisible comment--> text");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findTableOfContent() {
    val matcher = WikiSyntaxPatternList.TABLE_OF_CONTENTS.matcher("__TOC__");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDivs() {
    val matcher = WikiSyntaxPatternList.DIV.matcher("<div class=\"floatright\">Text on the right</div>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findTableOfBlockquote() {
    val matcher = WikiSyntaxPatternList.BLOCKQUOTE.matcher("<blockquote>"
        + "The '''blockquote''' tag will indent both margins when needed instead of the left margin only as the colon does."
        + "</blockquote>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHeadingsWithMultipleLevels() {
    val headings = ImmutableList.of(
        "== Heading Level 2 ==",
        "=== Heading Level 3 ===",
        "==== Heading Level 4 ====",
        "===== Heading Level 5 =====",
        "====== Heading Level 6 ======");

    for (String heading : headings) {
      val matcher = WikiSyntaxPatternList.HEADING.matcher(heading);
      assertThat(matcher.find()).isTrue();
    }
  }

  @Test
  void findPointInUnorderedList() {
    val matcher = WikiSyntaxPatternList.UNORDERED_LIST.matcher("* Point of a new list");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubpointInUnorderedList() {
    val matcher = WikiSyntaxPatternList.UNORDERED_LIST.matcher("** Subpoint of a list");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPointInOrderedList() {
    val matcher = WikiSyntaxPatternList.ORDERED_LIST.matcher("# Point of a new list");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubpointInOrderedList() {
    val matcher = WikiSyntaxPatternList.ORDERED_LIST.matcher("## Subpoint of a list");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDefinitionList() {
    val matcher = WikiSyntaxPatternList.DEFINITION_LIST.matcher("; Definition");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDescriptionInDefinitionList() {
    val matcher = WikiSyntaxPatternList.DEFINITION_LIST.matcher(": Definition description");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHorizontalRuleWiki() {
    val matcher = WikiSyntaxPatternList.HORIZONTAL_RULE_WIKI.matcher("----");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHorizontalRuleHTML() {
    val matcher = WikiSyntaxPatternList.HORIZONTAL_RULE_HTML.matcher("<hr />");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findInternalLinks() {
    val matcher = WikiSyntaxPatternList.LINKS_INTERN.matcher("[[test]]");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findExternalLinks() {
    val matcher = WikiSyntaxPatternList.LINKS_EXTERN.matcher("https://www.test.de/test");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefs1() {
    val matcher = WikiSyntaxPatternList.REFS.matcher("<ref name=\"test\">test</ref>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findRefs2() {
    val matcher = WikiSyntaxPatternList.REFS.matcher("<ref name=\"test\" />");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findReflist() {
    val matcher = WikiSyntaxPatternList.REF_LIST.matcher("<references />");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findNowiki1() {
    val matcher = WikiSyntaxPatternList.NOWIKI.matcher("<nowiki>test</nowiki>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findNowiki2() {
    val matcher = WikiSyntaxPatternList.NOWIKI.matcher("<nowiki />");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findNoinclude() {
    val matcher = WikiSyntaxPatternList.NOINCLUDE.matcher("<noinclude>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findIncludeonly() {
    val matcher = WikiSyntaxPatternList.INCLUDEONLY.matcher("<includeonly>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findOnlyinclude() {
    val matcher = WikiSyntaxPatternList.ONLYINCLUDE.matcher("<onlyinclude>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDl() {
    val matcher = WikiSyntaxPatternList.DL.matcher("<dl>test</dl>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDt() {
    val matcher = WikiSyntaxPatternList.DT.matcher("<dt>test</dt>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDd() {
    val matcher = WikiSyntaxPatternList.DD.matcher("<dd>test</dd>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findScore() {
    val matcher = WikiSyntaxPatternList.SCORE.matcher("<score>test</score>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBreak1() {
    val matcher = WikiSyntaxPatternList.BR.matcher("<br>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBreak2() {
    val matcher = WikiSyntaxPatternList.BR.matcher("<br />");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBreak() {
    val matcher = WikiSyntaxPatternList.BR.matcher("<br>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findS() {
    val matcher = WikiSyntaxPatternList.S.matcher("<s>test</s>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findU() {
    val matcher = WikiSyntaxPatternList.U.matcher("<u>test</u>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDel() {
    val matcher = WikiSyntaxPatternList.DEL.matcher("<del>test</del>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findIns() {
    val matcher = WikiSyntaxPatternList.INS.matcher("<ins>test</ins>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPre() {
    val matcher = WikiSyntaxPatternList.PRE.matcher("<pre name=\"test\">test</pre>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBig() {
    val matcher = WikiSyntaxPatternList.BIG.matcher("<big name=\"test\">test</big>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSmall() {
    val matcher = WikiSyntaxPatternList.SMALL.matcher("<small name=\"test\">test</small>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSyntaxhighlight() {
    val matcher = WikiSyntaxPatternList.SYNTAXHIGHLIGHT.matcher("<syntaxhighlight name=\"test\">test</syntaxhighlight>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSpan() {
    val matcher = WikiSyntaxPatternList.SPAN.matcher("<span name=\"test\">test</span>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCurlyBraces1() {
    val matcher = WikiSyntaxPatternList.CURLY_BRACES.matcher("{{test}}");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCurlyBraces2() {
    val matcher = WikiSyntaxPatternList.CURLY_BRACES.matcher("{{{test}}}");
    assertThat(matcher.find()).isTrue();
  }

}

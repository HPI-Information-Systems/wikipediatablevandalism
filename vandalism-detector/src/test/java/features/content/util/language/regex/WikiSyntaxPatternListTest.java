package features.content.util.language.regex;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;

class WikiSyntaxPatternListTest {

  @Test
  void findItalicText() {
    val matcher = WikiSyntaxPatternList.BOLD_ITALIC.matcher("'' this is italic''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBoldText() {
    val matcher = WikiSyntaxPatternList.BOLD_ITALIC.matcher("'''this is bold'''");
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
  void findtchUnclosedMathFormula() {
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
  void findTextStackedToTheRight() {
    val matcher = WikiSyntaxPatternList.STACK_TEXT.matcher("This text is {{stack|stacked on the right}}");
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
  void findLinks() {
    val matcher = WikiSyntaxPatternList.LINKS.matcher("[[test]]");
    assertThat(matcher.find()).isTrue();
  }

}

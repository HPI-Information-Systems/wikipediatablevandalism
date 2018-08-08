package features.content.util.table;

import java.util.regex.Pattern;
import lombok.val;

public class SyntaxChecker {

  static Pattern TABLE_OPEN_HTML = Pattern.compile("<table(.*?)>");
  static Pattern TABLE_CLOSE_HTML = Pattern.compile("</table>");
  static Pattern TABLE_OPEN_WIKI = Pattern.compile("\\{\\|");
  static Pattern TABLE_CLOSE_WIKI = Pattern.compile("\\|}");

  public static int checkTableClipCount(String content) {
    int clipCount = 0;

    val matcherOpenWiki = TABLE_OPEN_WIKI.matcher(content);
    val matcherOpenHTML = TABLE_OPEN_HTML.matcher(content);
    while (matcherOpenWiki.find() || matcherOpenHTML.find()) {
      ++clipCount;
    }
    val matcherCloseWiki = TABLE_CLOSE_WIKI.matcher(content);
    val matcherCloseHTML = TABLE_CLOSE_HTML.matcher(content);
    while (matcherCloseWiki.find() || matcherCloseHTML.find()) {
      --clipCount;
    }

    return clipCount;
  }

  static Pattern REF_HTML_OPEN = Pattern.compile("<ref(.*?)>", Pattern.DOTALL);
  static Pattern REF_HTML_CLOSE = Pattern.compile("</ref>");
  static Pattern REF_WIKI_OPEN = Pattern.compile("\\[\\[ref:");
  static Pattern REF_WIKI_CLOSE = Pattern.compile("]]");
  static Pattern LINK_OPEN = Pattern.compile("\\[\\[");
  //static Pattern LINK_CLOSE = Pattern.compile("]]");
  static Pattern BOLD_ITALIC = Pattern.compile("\'\'"); // gerade
  static Pattern MATH_FORMULA_OPEN = Pattern.compile("<math(.*?)>", Pattern.DOTALL);
  static Pattern MATH_FORMULA_CLOSE = Pattern.compile("</math>");
  static Pattern SUPERSCRIPT_OPEN = Pattern.compile("<sup>");
  static Pattern SUPERSCRIPT_CLOSE = Pattern.compile("</sup>");
  static Pattern SUBSCRIPT_OPEN = Pattern.compile("<sub>");
  static Pattern SUBSCRIPT_CLOSE = Pattern.compile("</sub>");
  static Pattern POEM_OPEN = Pattern.compile("<poem>");
  static Pattern POEM_CLOSE = Pattern.compile("</poem>");
  static Pattern CODE_OPEN = Pattern.compile("<code>");
  static Pattern CODE_CLOSE = Pattern.compile("</code>");
  static Pattern BLOCKQUOTE_OPEN = Pattern.compile("<blockquote>");
  static Pattern BLOCKQUOTE_CLOSE = Pattern.compile("</blockquote>");
  static Pattern DIV_OPEN = Pattern.compile("<div(.*?)>", Pattern.DOTALL);
  static Pattern DIV_CLOSE = Pattern.compile("</div>");
  static Pattern STACK_TEXT_OPEN = Pattern.compile("\\{\\{stack");
  static Pattern STACK_TEXT_CLOSE = Pattern.compile("}}");
  static Pattern TABLE_OF_CONTENTS = Pattern.compile("(__TOC__)");
  static Pattern COMMENT_OPEN = Pattern.compile("<!--");
  static Pattern COMMENT_CLOSE = Pattern.compile("-->");
  static Pattern HEADING = Pattern.compile("==");

  public static int checkOpenAndCloseSyntaxCount(String content) {
    int openCount = 0;

    val matcherOpenRefHTML = REF_HTML_OPEN.matcher(content);
    val matcherOpenRefWiki = REF_WIKI_OPEN.matcher(content);
    val matcherOpenLink = LINK_OPEN.matcher(content);
    val matcherOpenMath = MATH_FORMULA_OPEN.matcher(content);
    val matcherOpenSuperscript = SUPERSCRIPT_OPEN.matcher(content);
    val matcherOpenSubscript = SUBSCRIPT_OPEN.matcher(content);
    val matcherOpenPoem = POEM_OPEN.matcher(content);
    val matcherOpenCode = CODE_OPEN.matcher(content);
    val matcherOpenBlockquote = BLOCKQUOTE_OPEN.matcher(content);
    val matcherOpenDiv = DIV_OPEN.matcher(content);
    val matcherOpenStackText = STACK_TEXT_OPEN.matcher(content);
    val matcherOpenComment = COMMENT_OPEN.matcher(content);
    while (matcherOpenRefHTML.find() || matcherOpenRefWiki.find() || matcherOpenLink.find()
        || matcherOpenMath.find() || matcherOpenSuperscript.find() || matcherOpenSubscript.find()
        || matcherOpenPoem.find() || matcherOpenCode.find() || matcherOpenBlockquote.find()
        || matcherOpenDiv.find() || matcherOpenStackText.find()) {
      ++openCount;
    }

    val matcherCloseRefHTML = REF_HTML_CLOSE.matcher(content);
    val matcherCloseRefWiki = REF_WIKI_CLOSE.matcher(content);
    //val matcherLinkClose = LINK_CLOSE.matcher(content);
    val matcherCloseMath = MATH_FORMULA_CLOSE.matcher(content);
    val matcherCloseSuperscript = SUPERSCRIPT_CLOSE.matcher(content);
    val matcherCloseSubscript = SUBSCRIPT_CLOSE.matcher(content);
    val matcherClosePoem = POEM_CLOSE.matcher(content);
    val matcherCloseCode = CODE_CLOSE.matcher(content);
    val matcherCloseBlockquote = BLOCKQUOTE_CLOSE.matcher(content);
    val matcherCloseDiv = DIV_CLOSE.matcher(content);
    val matcherCloseStackText = STACK_TEXT_CLOSE.matcher(content);
    val matcherCloseComment = COMMENT_CLOSE.matcher(content);
    while (matcherCloseRefHTML.find() || matcherCloseRefWiki.find() /*|| matcherLinkClose.find()*/
        || matcherCloseMath.find() || matcherCloseSuperscript.find() || matcherCloseSubscript.find()
        || matcherClosePoem.find() || matcherCloseCode.find() || matcherCloseBlockquote.find()
        || matcherCloseDiv.find() || matcherCloseStackText.find() || matcherCloseComment.find()) {
      --openCount;
    }

    int boldItalicAndHeadingCount = 0;
    val matcherBoldItalic = BOLD_ITALIC.matcher(content);
    val matcherHeading = HEADING.matcher(content);
    while (matcherBoldItalic.find() || matcherHeading.find()) {
      ++boldItalicAndHeadingCount;
    }
    openCount += boldItalicAndHeadingCount % 2; // good when even

    int tocCount = 0;
    val matcherTOC = TABLE_OF_CONTENTS.matcher(content);
    while (matcherTOC.find()) {
      ++tocCount;
    }
    if (tocCount > 1) {
      openCount += tocCount - 1; // add too much table of contents as bad
    }

    return openCount;
  }

}

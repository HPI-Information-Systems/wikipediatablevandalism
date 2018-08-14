package features.content.util.table;

import java.util.regex.Pattern;
import lombok.val;

public class SyntaxChecker {

  static Pattern BOLD = Pattern.compile("\'\'"); // even
  static Pattern ITALIC = Pattern.compile("\'\'\'"); // even
  static Pattern BOLD_ITALIC = Pattern.compile("\'\'\'\'\'"); // even
  static Pattern REF_HTML_OPEN = Pattern.compile("(<ref(.*?)>)", Pattern.DOTALL);
  static Pattern REF_HTML_CLOSE = Pattern.compile("</ref>");
  static Pattern LINK_EXTERN_OPEN = Pattern.compile("\\[");
  static Pattern LINK_EXTERN_CLOSE = Pattern.compile("]");
  static Pattern CURLY_BRACES_OPEN = Pattern.compile("\\{");
  static Pattern CURLY_BRACES_CLOSE = Pattern.compile("}");
  static Pattern MATH_FORMULA_OPEN = Pattern.compile("(<math(.*?)>)", Pattern.DOTALL);
  static Pattern MATH_FORMULA_CLOSE = Pattern.compile("</math>");
  static Pattern SPAN_OPEN = Pattern.compile("(<span(.*?)>)", Pattern.DOTALL);
  static Pattern SPAN_CLOSE = Pattern.compile("</span>");
  static Pattern SYNTAXHIGHLIGHT_OPEN = Pattern.compile("(<syntaxhighlight(.*?)>)", Pattern.DOTALL);
  static Pattern SYNTAXHIGHLIGHT_CLOSE = Pattern.compile("</syntaxhighlight>");
  static Pattern SMALL_OPEN = Pattern.compile("(<small(.*?)>)", Pattern.DOTALL);
  static Pattern SMALL_CLOSE = Pattern.compile("</small>");
  static Pattern BIG_OPEN = Pattern.compile("(<big(.*?)>)", Pattern.DOTALL);
  static Pattern BIG_CLOSE = Pattern.compile("</big>");
  static Pattern DIV_OPEN = Pattern.compile("(<div(.*?)>)", Pattern.DOTALL);
  static Pattern DIV_CLOSE = Pattern.compile("</div>");
  static Pattern POEM_OPEN = Pattern.compile("(<poem(.*?)>)", Pattern.DOTALL);
  static Pattern POEM_CLOSE = Pattern.compile("</poem>");
  static Pattern PRE_OPEN = Pattern.compile("<pre>");
  static Pattern PRE_CLOSE = Pattern.compile("</pre>");
  static Pattern SUPERSCRIPT_OPEN = Pattern.compile("<sup>");
  static Pattern SUPERSCRIPT_CLOSE = Pattern.compile("</sup>");
  static Pattern S_OPEN = Pattern.compile("<s>");
  static Pattern S_CLOSE = Pattern.compile("</s>");
  static Pattern U_OPEN = Pattern.compile("<u>");
  static Pattern U_CLOSE = Pattern.compile("</u>");
  static Pattern DEL_OPEN = Pattern.compile("<del>");
  static Pattern DEL_CLOSE = Pattern.compile("</del>");
  static Pattern INS_OPEN = Pattern.compile("<ins>");
  static Pattern INS_CLOSE = Pattern.compile("</ins>");
  static Pattern SUBSCRIPT_OPEN = Pattern.compile("<sub>");
  static Pattern SUBSCRIPT_CLOSE = Pattern.compile("</sub>");
  static Pattern CODE_OPEN = Pattern.compile("<code>");
  static Pattern CODE_CLOSE = Pattern.compile("</code>");
  static Pattern BLOCKQUOTE_OPEN = Pattern.compile("<blockquote>");
  static Pattern BLOCKQUOTE_CLOSE = Pattern.compile("</blockquote>");
  static Pattern COMMENT_OPEN = Pattern.compile("<!--");
  static Pattern COMMENT_CLOSE = Pattern.compile("-->");
  static Pattern NOWIKI_OPEN = Pattern.compile("<nowiki>");
  static Pattern NOWIKI_CLOSE = Pattern.compile("</nowiki>");
  static Pattern DL_OPEN = Pattern.compile("<dl>");
  static Pattern DL_CLOSE = Pattern.compile("</dl>");
  static Pattern DT_OPEN = Pattern.compile("<dt>");
  static Pattern DT_CLOSE = Pattern.compile("</dt>");
  static Pattern DD_OPEN = Pattern.compile("<dd>");
  static Pattern DD_CLOSE = Pattern.compile("</dd>");
  static Pattern SCORE_OPEN = Pattern.compile("<score>");
  static Pattern SCORE_CLOSE = Pattern.compile("</score>");
  static Pattern HEADING2 = Pattern.compile("=="); // even
  static Pattern HEADING3 = Pattern.compile("==="); // even
  static Pattern HEADING4 = Pattern.compile("===="); // even
  static Pattern HEADING5 = Pattern.compile("====="); // even
  static Pattern HEADING6 = Pattern.compile("======"); // even

  public static double getOpenAndCloseSyntaxCount(String content) {

    content = replaceMatching2(content, "<!--", "-->");
    content = replaceMatching2(content, "<nowiki>", "</nowiki>");

    double syntaxCount = 0;
    val matcherRefHTMLOpen = REF_HTML_OPEN.matcher(content);
    val matcherRefWikiOpen = LINK_EXTERN_OPEN.matcher(content);
    val matcherCurlyBracesOpen = CURLY_BRACES_OPEN.matcher(content);
    val matcherMathOpen = MATH_FORMULA_OPEN.matcher(content);
    val matcherSpanOpen = SPAN_OPEN.matcher(content);
    val matcherSyntaxhighlightOpen = SYNTAXHIGHLIGHT_OPEN.matcher(content);
    val matcherSmallOpen = SMALL_OPEN.matcher(content);
    val matcherBigOpen = BIG_OPEN.matcher(content);
    val matcherDivOpen = DIV_OPEN.matcher(content);
    val matcherPoemOpen = POEM_OPEN.matcher(content);
    val matcherPreOpen = PRE_OPEN.matcher(content);
    val matcherSuperscriptOpen = SUPERSCRIPT_OPEN.matcher(content);
    val matcherSOpen = S_OPEN.matcher(content);
    val matcherUOpen = U_OPEN.matcher(content);
    val matcherDelOpen = DEL_OPEN.matcher(content);
    val matcherInsOpen = INS_OPEN.matcher(content);
    val matcherSubscriptOpen = SUBSCRIPT_OPEN.matcher(content);
    val matcherCodeOpen = CODE_OPEN.matcher(content);
    val matcherBlockquoteOpen = BLOCKQUOTE_OPEN.matcher(content);
    val matcherCommentOpen = COMMENT_OPEN.matcher(content);
    val matcherNowikiOpen = NOWIKI_OPEN.matcher(content);
    val matcherDlOpen = DL_OPEN.matcher(content);
    val matcherDtOpen = DT_OPEN.matcher(content);
    val matcherDdOpen = DD_OPEN.matcher(content);
    val matcherScoreOpen = SCORE_OPEN.matcher(content);
    while (matcherRefHTMLOpen.find() || matcherRefWikiOpen.find() || matcherCurlyBracesOpen
        .find() || matcherMathOpen.find() || matcherSpanOpen.find() || matcherSyntaxhighlightOpen
        .find() || matcherSmallOpen.find() || matcherBigOpen.find() || matcherDivOpen.find()
        || matcherPoemOpen.find() || matcherPreOpen.find() || matcherSuperscriptOpen.find()
        || matcherSOpen.find() || matcherUOpen.find() || matcherDelOpen.find() || matcherInsOpen
        .find() || matcherSubscriptOpen.find() || matcherCodeOpen.find() || matcherBlockquoteOpen
        .find() || matcherCommentOpen.find() || matcherNowikiOpen.find() || matcherDlOpen.find()
        || matcherDtOpen.find() || matcherDdOpen.find() || matcherScoreOpen.find()) {
      ++syntaxCount;
    }

    val matcherRefHTMLClose = REF_HTML_CLOSE.matcher(content);
    val matcherRefWikiClose = LINK_EXTERN_CLOSE.matcher(content);
    val matcherCurlyBracesClose = CURLY_BRACES_CLOSE.matcher(content);
    val matcherMathClose = MATH_FORMULA_CLOSE.matcher(content);
    val matcherSpanClose = SPAN_CLOSE.matcher(content);
    val matcherSyntaxhighlightClose = SYNTAXHIGHLIGHT_CLOSE.matcher(content);
    val matcherSmallClose = SMALL_CLOSE.matcher(content);
    val matcherBigClose = BIG_CLOSE.matcher(content);
    val matcherDivClose = DIV_CLOSE.matcher(content);
    val matcherPoemClose = POEM_CLOSE.matcher(content);
    val matcherPreClose = PRE_CLOSE.matcher(content);
    val matcherSuperscriptClose = SUPERSCRIPT_CLOSE.matcher(content);
    val matcherSClose = S_CLOSE.matcher(content);
    val matcherUClose = U_CLOSE.matcher(content);
    val matcherDelClose = DEL_CLOSE.matcher(content);
    val matcherInsClose = INS_CLOSE.matcher(content);
    val matcherSubscriptClose = SUBSCRIPT_CLOSE.matcher(content);
    val matcherCodeClose = CODE_CLOSE.matcher(content);
    val matcherBlockquoteClose = BLOCKQUOTE_CLOSE.matcher(content);
    val matcherCommentClose = COMMENT_CLOSE.matcher(content);
    val matcherNowikiClose = NOWIKI_CLOSE.matcher(content);
    val matcherDlClose = DL_CLOSE.matcher(content);
    val matcherDtClose = DT_CLOSE.matcher(content);
    val matcherDdClose = DD_CLOSE.matcher(content);
    val matcherScoreClose = SCORE_CLOSE.matcher(content);
    while (matcherRefHTMLClose.find() || matcherRefWikiClose.find() || matcherCurlyBracesClose
        .find() || matcherMathClose.find() || matcherSpanClose.find() || matcherSyntaxhighlightClose
        .find() || matcherSmallClose.find() || matcherBigClose.find() || matcherDivClose.find()
        || matcherPoemClose.find() || matcherPreClose.find() || matcherSuperscriptClose.find()
        || matcherSClose.find() || matcherUClose.find() || matcherDelClose.find() || matcherInsClose
        .find() || matcherSubscriptClose.find() || matcherCodeClose.find() || matcherBlockquoteClose
        .find() || matcherCommentClose.find() || matcherNowikiClose.find() || matcherDlClose
        .find() || matcherDtClose.find() || matcherDdClose.find() || matcherScoreClose.find()) {
      --syntaxCount;
    }

    int boldItalicAndHeadingCount = 0;
    val matcherBold = BOLD.matcher(content);
    val matcherItalic = ITALIC.matcher(content);
    val matcherBoldItalic = BOLD_ITALIC.matcher(content);
    val matcherHeading2 = HEADING2.matcher(content);
    val matcherHeading3 = HEADING3.matcher(content);
    val matcherHeading4 = HEADING4.matcher(content);
    val matcherHeading5 = HEADING5.matcher(content);
    val matcherHeading6 = HEADING6.matcher(content);
    while (matcherBold.find() || matcherItalic.find() || matcherBoldItalic.find() || matcherHeading2
        .find() || matcherHeading3.find() || matcherHeading4.find() || matcherHeading5.find()
        || matcherHeading6.find()) {
      ++boldItalicAndHeadingCount;
    }
    syntaxCount += boldItalicAndHeadingCount % 2; // good when even

    return syntaxCount;
  }

  private static String replaceMatching2(String input, String lowerBound, String upperBound) {
    return input
        .replaceAll("(.*?" + lowerBound + ")" + "(.*?)" + "(" + upperBound + ".*)", "$1$3");
  }

}

package features.content.util.table;

import java.util.regex.Pattern;
import lombok.val;

public class SyntaxChecker {

  static Pattern TABLE_OPEN_HTML = Pattern.compile("<table(\\s+\\p{Graph}+\\s*=\\s*\"\\s*\\p{Graph}+\\s*\")*\\s*>");
  static Pattern TABLE_CLOSE_HTML = Pattern.compile("</\\s*t\\s*a\\s*b\\s*l\\s*e\\s*>");
  static Pattern TABLE_OPEN_WIKI = Pattern.compile("\\{\\|");
  static Pattern TABLE_CLOSE_WIKI = Pattern.compile("\\|}");

  public static int checkClipCount(String content) {
    int clipCount = 0;

    //count wiki style table clips
    val matcherOpenWiki = TABLE_OPEN_WIKI.matcher(content);
    while (matcherOpenWiki.find()) {
      ++clipCount;
    }
    val matcherCloseWiki = TABLE_CLOSE_WIKI.matcher(content);
    while (matcherCloseWiki.find()) {
      --clipCount;
    }

    //count html table clips
    val matcherOpenHTML = TABLE_OPEN_HTML.matcher(content);
    while (matcherOpenHTML.find()) {
      ++clipCount;
    }
    val matcherCloseHTML = TABLE_CLOSE_HTML.matcher(content);
    while (matcherCloseHTML.find()) {
      --clipCount;
    }

    return clipCount;
  }

}

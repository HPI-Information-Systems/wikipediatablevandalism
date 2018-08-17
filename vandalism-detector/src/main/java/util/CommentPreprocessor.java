package util;

import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.sweble.wikitext.dumpreader.export_0_10.CommentType;

public class CommentPreprocessor {

  /**
   * Sometimes the headline of the section that was edited is included in the revision comment by
   * default. We do not assume that users voluntarily type section headlines in C-style comments
   * next to their remark.
   *
   * Example: https://en.wikipedia.org/w/index.php?diff=235131990; Comment was &#47;* Cycles *&#47;
   */
  private static final Pattern GENERATED_STUB = Pattern.compile("^\\/\\*.*?\\*\\/");

  /**
   * Filter auto-generated content containing user names etc.
   *
   * Example: https://en.wikipedia.org/w/index.php?title=Wizards_of_Waverly_Place&diff=212809563
   * Comment was "[[WP:UNDO|Undid]] revision 212809476 by [[Special:Contributions/Anfish|Anfish]]
   * ([[User talk:Anfish|talk]])"
   */
  private static final Pattern COMMENT_TAG = Pattern.compile("\\[\\[.*?:.*?\\]\\]");

  public static String extractUserComment(@Nullable final CommentType comment) {
    if (isBlank(comment) || isGenerated(comment)) {
      return "";
    }

    return removeStub(comment);
  }

  public static boolean isBlank(@Nullable final CommentType comment) {
    return comment == null || StringUtils.isBlank(comment.getValue());
  }

  public static boolean isGenerated(final CommentType comment) {
    return COMMENT_TAG.matcher(comment.getValue()).find();
  }

  public static String removeStub(final CommentType comment) {
    return GENERATED_STUB.matcher(comment.getValue()).replaceFirst("");
  }
}

package features.content.util.table;

public class SyntaxChecker {

  public static int checkClipCount(String content) {
    int clipCount = 0;
    for (Character c : content.toCharArray()) {
      if (c.compareTo('{') == 0) {
        ++clipCount;
      } else if (c.compareTo('}') == 0) {
        --clipCount;
      }
    }
    return clipCount;
  }

}

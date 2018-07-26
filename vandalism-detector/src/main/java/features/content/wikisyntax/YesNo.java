package features.content.wikisyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import wikixmlsplit.renderer.wikitable.Cell;

class YesNo extends TemplateUseImpact {

  // https://en.wikipedia.org/wiki/Template:Yes
  private static final Pattern PATTERN = Pattern.compile("(?i)\\{\\{(yes|no|\\?|y|n)}}");

  @Override
  protected Stream<String> extractTemplatesFromCell(Cell cell) {
    final List<String> groups = new ArrayList<>();
    final Matcher matcher = PATTERN.matcher(cell.getValue());
    while (matcher.find()) {
      groups.add(matcher.group());
    }
    return groups.stream();
  }
}

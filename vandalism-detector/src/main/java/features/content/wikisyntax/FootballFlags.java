package features.content.wikisyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import wikixmlsplit.renderer.wikitable.Cell;

/**
 * Recognize the shorthand for flags of FIFA recognized countries.
 */
class FootballFlags extends TemplateUseImpact {

  private static final Pattern PATTERN = Pattern.compile("\\{\\{([a-zA-Z]{3})}}");

  @Override
  protected Stream<String> extractTemplatesFromCell(Cell cell) {
    final List<String> groups = new ArrayList<>();
    final Matcher matcher = PATTERN.matcher(cell.getValue());
    while (matcher.find()) {
      final String group = matcher.group(1);
      if (FifaCountryList.getCountries().contains(group)) {
        groups.add(group);
      }
    }
    return groups.stream();
  }
}

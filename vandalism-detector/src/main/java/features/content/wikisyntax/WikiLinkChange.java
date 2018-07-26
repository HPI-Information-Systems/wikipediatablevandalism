package features.content.wikisyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import wikixmlsplit.renderer.wikitable.Cell;

/**
 * Track the amount of Inter-Wiki Link Changes.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Help:Link#Wikilinks_(internal_links)">Wikilink
 * Docs</a>
 */
class WikiLinkChange extends TemplateUseImpact {

  private static final Pattern LINK_PATTERN = Pattern.compile("(\\[\\[.*?\\]\\])");

  @Override
  protected Stream<String> extractTemplatesFromCell(Cell cell) {
    final List<String> groups = new ArrayList<>();
    final Matcher matcher = LINK_PATTERN.matcher(cell.getValue());
    while (matcher.find()) {
      groups.add(matcher.group());
    }
    return groups.stream();
  }
}

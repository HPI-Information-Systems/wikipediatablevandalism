package util;

import static java.util.stream.Collectors.toSet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import java.util.Set;
import wikixmlsplit.renderer.wikitable.Attribute;

public class AttributeUtil {

  private static final Set<String> ATTRIBUTE_NAMES;

  static {
    ATTRIBUTE_NAMES = ImmutableSet.<String>builder()
        .addAll(tableAttributes())
        .addAll(rowAttributes())
        .addAll(cellAttributes())
        .addAll(genericAttributes())
        .build();
  }

  // https://www.w3.org/TR/html401/struct/tables.html#h-11.2.1
  private static Set<String> tableAttributes() {
    return ImmutableSet.of(
        "summary", "width", "border", "frame", "rules", "cellspacing", "cellpadding");
  }

  // https://www.w3.org/TR/html401/struct/tables.html#h-11.2.5
  private static Set<String> rowAttributes() {
    return ImmutableSet.of("cellhalign", "cellvalign");
  }

  // https://www.w3.org/TR/html401/struct/tables.html#h-11.2.6
  private static Set<String> cellAttributes() {
    return ImmutableSet.of(
        "abbr", "axis", "headers", "scope", "rowspan", "colspan", "cellhalign", "cellvalign");
  }

  private static Set<String> genericAttributes() {
    return ImmutableSet.<String>builder()
        .add("id", "class", "style", "title") // coreattrs
        .add("lang", "dir") // I18N
        .add("onclick", "ondblclick", "onmousedown", "onmouseup", "onmouseover", "onmousemove",
            "onmouseout", "onkeypress", "onkeydown", "onkeyup") // events
        .add("bgcolor") // background color
        .add("align", "char", "charoff", "valign") // cell alignment
        .build();
  }

  static Multiset<String> extractNonStandardAttributeNames(final Set<Attribute> attributes) {
    final Set<Attribute> nonStandard = filterNonStandardAttributes(attributes);
    final Multiset<String> set = HashMultiset.create();
    for (final Attribute attribute : nonStandard) {
      set.add(attribute.getKey());
    }
    return set;
  }

  public static Set<Attribute> filterNonStandardAttributes(final Set<Attribute> attributes) {
    return attributes.stream().filter(a -> !ATTRIBUTE_NAMES.contains(a.getKey()))
        .collect(toSet());
  }

  public static boolean isValidAttribute(final Attribute attribute) {
    return ATTRIBUTE_NAMES.contains(attribute.getKey());
  }

}

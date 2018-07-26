package features.content.wikisyntax;

import java.util.Set;
import util.WordListUtil;

/**
 * List of FIFA-recognized countries.
 *
 * <p>Since football flags have the shorthand {@code {{ABC}}} which does not possess a prefix or
 * similar, it is required to validate the three-letter regex match against a complete list of
 * country abbreviations.</p>
 */
class FifaCountryList {

  // https://en.wikipedia.org/wiki/List_of_FIFA_country_codes
  private static final String FILENAME = "fifa-country-codes.txt";

  private static final Set<String> COUNTRIES = WordListUtil.read(FILENAME);

  static Set<String> getCountries() {
    return COUNTRIES;
  }
}

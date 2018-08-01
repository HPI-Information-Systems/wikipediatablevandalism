package util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.val;

public final class StemmerUtils {

  public static Multiset<String> stem(Multiset<String> words) {
    val stemmer = new Stemmer();
    return words.stream()
        .map(stemmer::stem)
        .collect(Collectors.toCollection(HashMultiset::create));
  }

  public static Set<String> stem(Set<String> words) {
    val stemmer = new Stemmer();
    return words.stream()
        .map(stemmer::stem)
        .collect(Collectors.toSet());
  }

  public static String stem(String word) {
    val stemmer = new Stemmer();
    return stemmer.stem(word);
  }

  private StemmerUtils() {
  }
}

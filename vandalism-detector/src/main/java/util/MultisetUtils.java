package util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.Set;
import lombok.val;

public class MultisetUtils {

  public static <T> Multiset<T> getMatches(Multiset<T> multiset, Set<T> wanted,
      boolean isMatching) {
    val matches = HashMultiset.create(multiset);

    if (isMatching) {
      matches.retainAll(wanted);
    } else {
      matches.removeAll(wanted);
    }

    return matches;
  }
}

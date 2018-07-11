package util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import lombok.val;

public class KLDUtil {

  public static Double calculateKLDOfAddedChars(final String before, final String current) {
    Multiset<Character> beforeCharDistributionMultiSet = HashMultiset.create();
    Multiset<Character> currentCharDistributionMultiSet = HashMultiset.create();
    for (char c : before.toCharArray()) {
      beforeCharDistributionMultiSet.add(c);
    }
    for (char c : current.toCharArray()) {
      currentCharDistributionMultiSet.add(c);
    }
    val addedCharDistributionMultiSet = Multisets
        .difference(currentCharDistributionMultiSet, beforeCharDistributionMultiSet);
    return calculateKLD(beforeCharDistributionMultiSet, addedCharDistributionMultiSet);
  }

  /*
      P(c) = probability of character in beforeCharDistributionMultiSet
      Q(c) = probability of character in addedCharDistributionMultiSet
      KLD = sum for all chars c in beforeCharDistributionMultiSet -> P(c) * log2 (P(c) / Q(c))
   */
  private static Double calculateKLD(final Multiset<Character> beforeCharDistributionMultiSet,
      final Multiset<Character> addedCharDistributionMultiSet) {
    Double result = 0.0;
    for (val c : addedCharDistributionMultiSet) {
      Double p =
          (double) addedCharDistributionMultiSet.count(c) / addedCharDistributionMultiSet.size();
      Double q = Double.MIN_VALUE;
      if (beforeCharDistributionMultiSet.contains(c)) {
        p = (double) beforeCharDistributionMultiSet.count(c) / beforeCharDistributionMultiSet
            .size();
      }
      Double pDivQ = p / q;
      if (pDivQ == Double.POSITIVE_INFINITY) {
        pDivQ = Double.MAX_VALUE;
      }
      if (pDivQ == Double.NEGATIVE_INFINITY) {
        pDivQ = -Double.MAX_VALUE;
      }
      Double log1 = Math.log(pDivQ);
      Double log2 = log1 / Math.log(2);
      Double res = p * log2;
      result += res;
      // result a= p * (Math.log(p / q) / Math.log(2)); // log2 because of bit representation
    }
    return result;
  }

}

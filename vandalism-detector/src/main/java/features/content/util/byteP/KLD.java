package features.content.util.byteP;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import lombok.val;

public class KLD {

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
      PTc) = probability of character in charDistributionMultiSetTest
      PQ(c) = probability of character in charDistributionMultiSetBasic
      KLD = sum for all chars c in charDistributionMultiSetTest -> PT(c) * log2 (PT(c) / QT(c))
   */
  private static Double calculateKLD(final Multiset<Character> charDistributionMultiSetBasic,
      final Multiset<Character> charDistributionMultiSetTest) {
    Double result = 0.0;
    for (val c : charDistributionMultiSetTest.elementSet()) {
      Double pt =
          (double) charDistributionMultiSetTest.count(c) / charDistributionMultiSetTest.size();
      Double qt = Double.MIN_VALUE; // when char not contained in basic, assume minimal possible probability
      if (charDistributionMultiSetBasic.contains(c)) {
        qt = (double) charDistributionMultiSetBasic.count(c) / charDistributionMultiSetBasic
            .size();
      }
      Double ptDivQt = pt / qt;
      if (ptDivQt == Double.POSITIVE_INFINITY) {
        ptDivQt = Double.MAX_VALUE;
      }
      if (ptDivQt == Double.NEGATIVE_INFINITY) {
        ptDivQt = -Double.MAX_VALUE;
      }
      result += pt * (Math.log(ptDivQt) / Math.log(2)); // log2 because of bit representation
    }
    return result;
  }

}

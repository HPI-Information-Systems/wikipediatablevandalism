package features.content.util.byteP;

import static com.google.common.primitives.Chars.asList;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.primitives.Chars;
import model.FeatureParameters;

public class KLD {

  public static double kldOfAddedChars(final FeatureParameters parameters) {
    return calculateKLDOfAddedChars(parameters.getPreviousContent(), parameters.getContent());
  }

  public static double kld(final FeatureParameters parameters) {
    return calculateKLD(parameters.getPreviousContent(), parameters.getContent());
  }

  @VisibleForTesting
  static double calculateKLDOfAddedChars(final String before, final String current) {
    final Multiset<Character> charsBefore = toMultiset(before);
    final Multiset<Character> charsCurrent = toMultiset(current);
    final Multiset<Character> charsAdded = Multisets.difference(charsCurrent, charsBefore);
    return kld(charsBefore, charsAdded);
  }

  @VisibleForTesting
  static double calculateKLD(final String before, final String current) {
    final Multiset<Character> charsBefore = toMultiset(before);
    final Multiset<Character> charsCurrent = toMultiset(current);
    return kld(charsBefore, charsCurrent);
  }

  private static Multiset<Character> toMultiset(final String value) {
    return HashMultiset.create(asList(value.toCharArray()));
  }


  private static double kld(final Multiset<Character> previous, final Multiset<Character> current) {
    if (previous.equals(current)) {
      return 0;
    }

    final char[] chars = Chars.toArray(current.elementSet());

    final double[] probCurrent = prob(chars, current);
    final double[] probPrevious = prob(chars, resample(chars, previous));

    final double previousMinProbability = getNonZeroMin(probPrevious) / 2;

    double sum = 0;
    for (int index = 0; index < chars.length; ++index) {
      final double v = probCurrent[index] /
          (probPrevious[index] == 0 ? previousMinProbability : probPrevious[index]);
      sum += (v * Math.log(v) / Math.log(2));
    }
    return sum;
  }

  private static double getNonZeroMin(final double[] values) {
    double min = Double.MAX_VALUE;
    for (double v : values) {
      if (v > 0 && v < min) {
        min = v;
      }
    }
    return (min == Double.MAX_VALUE) ? 0.01 : min;
  }

  private static Multiset<Character> resample(final char[] chars, final Multiset<Character> set) {
    final Multiset<Character> subset = HashMultiset.create(set);
    subset.retainAll(asList(chars));
    return subset;
  }

  private static double[] prob(final char[] toTest, final Multiset<Character> chars) {
    final double[] prob = new double[toTest.length];
    if (chars.isEmpty()) {
      return prob;
    }

    for (int index = 0; index < toTest.length; ++index) {
      prob[index] = (double) chars.count(toTest[index]) / chars.size();
    }
    return prob;
  }
}

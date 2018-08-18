package features.context.impl;

import features.Feature;
import java.time.Duration;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import model.FeatureParameters;
import util.RatioUtil;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Model editor activity w.r.t. the preceding time frame.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EditActivity implements Feature {

  private final RatioUtil.Method method;
  private final Duration maxAge;

  @Override
  public double getValue(FeatureParameters parameters) {
    // Edits of last week
    final RevisionProvider recentProvider = RevisionProvider.maxAge(maxAge);
    final List<MyRevisionType> recent = recentProvider.get(parameters);

    // (Edits of last two weeks) - (edits of last week)
    final RevisionProvider referenceProvider = RevisionProvider.maxAge(maxAge.multipliedBy(2));
    final List<MyRevisionType> reference = referenceProvider.get(parameters);
    reference.removeAll(recent);

    return method.apply(reference.size(), recent.size());
  }

  public static EditActivity increaseComparingDurationOf(final Duration duration) {
    return new EditActivity(RatioUtil::added, duration);
  }

  public static EditActivity decreaseComparingDurationOf(final Duration duration) {
    return new EditActivity(RatioUtil::removed, duration);
  }
}

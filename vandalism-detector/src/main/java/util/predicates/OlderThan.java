package util.predicates;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import wikixmlsplit.datastructures.MyRevisionType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OlderThan implements Predicate<MyRevisionType> {

  private final Instant reference;

  @Override
  public boolean test(final MyRevisionType revision) {
    final Instant toTest = revision.getDate().toInstant();
    return toTest.isBefore(reference);
  }

  public static OlderThan ofRevisionAndMaxAge(final MyRevisionType revision,
      final TemporalAmount maxAge) {

    final Instant now = revision.getDate().toInstant();
    final Instant reference = now.minus(maxAge);
    return new OlderThan(reference);
  }
}

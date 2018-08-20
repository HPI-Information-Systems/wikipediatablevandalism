package features.context.impl;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import model.FeatureParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wikixmlsplit.datastructures.MyRevisionType;

class EditActivityTest {

  private final Instant now = Instant.now();
  private EditActivity activity;

  @Nested
  class Increase {

    @BeforeEach
    void setUp() {
      activity = EditActivity.increaseComparingDurationOf(Duration.ofHours(5));
    }

    @Test
    void stable() {
      final List<MyRevisionType> revisions = asList(revisionOfAge(8), revisionOfAge(2));
      final FeatureParameters params = getParams(revisions);

      double result = activity.getValue(params);

      assertThat(result).isZero();
    }

    @Test
    void increase() {
      final List<MyRevisionType> revisions = asList(revisionOfAge(8), revisionOfAge(3),
          revisionOfAge(2));
      final FeatureParameters params = getParams(revisions);

      double result = activity.getValue(params);

      assertThat(result).isEqualTo(2);
    }
  }

  @Nested
  class Decrease {

    @BeforeEach
    void setUp() {
      activity = EditActivity.decreaseComparingDurationOf(Duration.ofHours(5));
    }

    @Test
    void decrease() {
      final List<MyRevisionType> revisions = asList(revisionOfAge(8), revisionOfAge(7),
          revisionOfAge(2));
      final FeatureParameters params = getParams(revisions);

      double result = activity.getValue(params);

      assertThat(result).isEqualTo(0.5);
    }

  }

  private MyRevisionType revisionOfAge(final int age) {
    return new MyRevisionType() {
      @Override
      public Date getDate() {
        return new Date(now.minus(Duration.ofHours(age)).toEpochMilli());
      }
    };
  }

  private FeatureParameters getParams(final List<MyRevisionType> previous) {
    return FeatureParameters.builder()
        .previousRevisions(previous)
        .revision(revisionOfAge(0))
        .build();
  }
}
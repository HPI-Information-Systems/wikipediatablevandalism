package features.context.impl;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import lombok.val;
import model.FeatureParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wikixmlsplit.datastructures.MyRevisionType;


class RevisionProviderTest {

  private RevisionProvider provider;

  private List<MyRevisionType> revisions;
  private Instant now;

  @BeforeEach
  void setUp() {
    revisions = tenRevisions();
    now = Instant.now();
  }

  @Nested
  class AllRevisions {

    @BeforeEach
    void setUp() {
      provider = RevisionProvider.all();
    }

    @Test
    void shouldRetrieveAll() {
      assertThat(provider.get(params())).isEqualTo(revisions);
    }
  }

  @Nested
  class LastN {

    @BeforeEach
    void setUp() {
      provider = RevisionProvider.lastN(5);
    }

    @Test
    void shouldLimit() {
      assertThat(provider.get(params())).isEqualTo(revisions.subList(0, 5));
    }
  }

  @Nested
  class MaxAge {

    @BeforeEach
    void setUp() {
      provider = RevisionProvider.maxAge(Duration.ofSeconds(5));
    }

    @Test
    void shouldDiscardOlderRevisions() {
      val actual = provider.get(params());

      assertThat(actual).isEqualTo(revisions.subList(0, 5));
    }
  }

  @Nested
  class LastNWithMaxAge {

    @BeforeEach
    void setUp() {
      provider = RevisionProvider.lastNWithMaxAge(2, Duration.ofSeconds(5));
    }

    @Test
    void shouldLimitRecentRevisions() {
      val actual = provider.get(params());

      assertThat(actual).isEqualTo(revisions.subList(0, 2));
    }
  }

  private FeatureParameters params() {
    return FeatureParameters.builder()
        .revision(revisionWithAge(0))
        .previousRevisions(ImmutableList.copyOf(revisions))
        .build();
  }

  private List<MyRevisionType> tenRevisions() {
    return IntStream.rangeClosed(1, 10)
        .mapToObj(RevisionProviderTest.this::revisionWithAge)
        .collect(toList());
  }

  private MyRevisionType revisionWithAge(final int age) {
    return new MyRevisionType() {
      @Override
      public Date getDate() {
        return new Date(now.minusSeconds(age).toEpochMilli());
      }
    };
  }
}
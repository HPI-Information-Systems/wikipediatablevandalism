package util.predicates;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import lombok.val;
import org.junit.jupiter.api.Test;
import wikixmlsplit.datastructures.MyRevisionType;

class OlderThanTest {

  private final Instant now = Instant.now();

  @Test
  void olderThanTrue() {
    val predicate = OlderThan.ofRevisionAndMaxAge(revisionOfAge(5), Duration.ofSeconds(5));

    assertThat(predicate).accepts(revisionOfAge(11));
  }

  @Test
  void olderThanFalse() {
    val predicate = OlderThan.ofRevisionAndMaxAge(revisionOfAge(5), Duration.ofSeconds(5));

    assertThat(predicate)
        .rejects(revisionOfAge(10))
        .rejects(revisionOfAge(3));
  }

  private MyRevisionType revisionOfAge(final int age) {
    return new MyRevisionType() {
      @Override
      public Date getDate() {
        return new Date(now.minus(Duration.ofSeconds(age)).toEpochMilli());
      }
    };
  }
}
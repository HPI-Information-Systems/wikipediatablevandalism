package util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RatioUtilTest {

  @Test
  void removedRatio_removed() {
    assertThat(RatioUtil.removed(10, 5))
        .as("Half of references removed")
        .isEqualTo(0.5);
  }

  @Test
  void removedRatio_added() {
    assertThat(RatioUtil.removed(5, 10))
        .as("Only references added")
        .isZero();
  }

  @Test
  void removedRatio_previouslyZero() {
    assertThat(RatioUtil.removed(0, 10)).isZero();
  }

  @Test
  void removedRatio_afterwardsZero() {
    assertThat(RatioUtil.removed(10, 0)).isOne();
  }

  @Test
  void removedRatio_blank() {
    assertThat(RatioUtil.removed(0, 0)).isZero();
  }

  @Test
  void addedRatio_removed() {
    assertThat(RatioUtil.added(10, 5))
        .as("Half of references removed")
        .isZero();
  }

  @Test
  void addedRatio_added() {
    assertThat(RatioUtil.added(5, 10))
        .as("Only references added")
        .isEqualTo(2);
  }

  @Test
  void addedRatio_previouslyZero() {
    assertThat(RatioUtil.added(0, 10)).isEqualTo(10);
  }

  @Test
  void addedRatio_afterwardsZero() {
    assertThat(RatioUtil.added(10, 0)).isZero();
  }

  @Test
  void addedRatio_blank() {
    assertThat(RatioUtil.added(0, 0)).isZero();
  }
}

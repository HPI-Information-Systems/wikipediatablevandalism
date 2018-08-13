package features.content.util.table;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.Test;

class EmptyCellChangeRatioTest {

  @Test
  void findEmpty() {
    val matcher = EmptyCellChange.EMPTY_CELL_PATTERN.matcher("");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findWhitespace() {
    val matcher = EmptyCellChange.EMPTY_CELL_PATTERN.matcher("   ");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMinus() {
    val matcher = EmptyCellChange.EMPTY_CELL_PATTERN.matcher("-");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findTBA() {
    val matcher = EmptyCellChange.EMPTY_CELL_PATTERN.matcher("TBA");
    assertThat(matcher.find()).isTrue();
  }

}

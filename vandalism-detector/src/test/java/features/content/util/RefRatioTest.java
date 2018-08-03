package features.content.util;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.Test;

class RefRatioTest {

  @Test
  void findWikiOpen() {
    val matcher = RefRatio.REF.matcher("<ref name=\"Chomsky\">www.test.de</ref>");
    assertThat(matcher.find()).isTrue();
  }

}

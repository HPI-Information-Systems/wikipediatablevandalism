package features.content.util;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.Test;

class RefChangeRatioTest {

  @Test
  void findHtmlReference() {
    val matcher = RefChange.REF_HTML
        .matcher("Lorem Ipsum<ref name=\"Chomsky\">www.test.de</ref>Dolor");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findWikiReference() {
    val matcher = RefChange.REF_WIKI_SYNTAX
        .matcher("Obama PolitickerME[[ref:name=pme-noneofusurl=[http://www.politickerme.com/"
            + "jessicaalaimo/1316/none-us-delegate-stuff None of us like this delegate stuff], "
            + "PolitickerME, 14 April 2008.]] 01 Jun 2008");
    assertThat(matcher.find()).isTrue();
  }
}

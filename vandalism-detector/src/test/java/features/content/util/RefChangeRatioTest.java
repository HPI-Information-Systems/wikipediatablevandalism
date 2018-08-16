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

  @Test
  void removedRatio_removedReferences() {
    assertThat(RefChange.getRemovedRatio(10, 5))
        .as("Half of references removed")
        .isEqualTo(0.5);
  }

  @Test
  void removedRatio_addedReferences() {
    assertThat(RefChange.getRemovedRatio(5, 10))
        .as("Only references added")
        .isZero();
  }

  @Test
  void removedRatio_previouslyZero() {
    assertThat(RefChange.getRemovedRatio(0, 10)).isZero();
  }

  @Test
  void removedRatio_afterwardsZero() {
    assertThat(RefChange.getRemovedRatio(10, 0)).isOne();
  }

  @Test
  void removedRatio_noReferences() {
    assertThat(RefChange.getRemovedRatio(0, 0)).isZero();
  }

  @Test
  void addedRatio_removedReferences() {
    assertThat(RefChange.getAddedRatio(10, 5))
        .as("Half of references removed")
        .isZero();
  }

  @Test
  void addedRatio_addedReferences() {
    assertThat(RefChange.getAddedRatio(5, 10))
        .as("Only references added")
        .isEqualTo(2);
  }

  @Test
  void addedRatio_previouslyZero() {
    assertThat(RefChange.getAddedRatio(0, 10)).isOne();
  }

  @Test
  void addedRatio_afterwardsZero() {
    assertThat(RefChange.getAddedRatio(10, 0)).isZero();
  }

  @Test
  void addedRatio_noReferences() {
    assertThat(RefChange.getAddedRatio(0, 0)).isZero();
  }
}

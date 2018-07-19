package util;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import mock.MockTables;
import org.junit.jupiter.api.Test;

class WordsExtractorTest {

  @Test
  void diffReturnsWordsIntroducedInNewRevision() {
    val previousTable = MockTables.NEPAL_13809326_Table_0;
    val table = MockTables.NEPAL_13809566_Table_0;
    val diff = WordsExtractor.diffWords(previousTable, table);

    assertThat(diff).containsExactlyInAnyOrder("purple", "duckie", "i", "am", "dumb");
  }

  @Test
  void diffReturnsNoWordsWhenComparingTheSameRevision() {
    val table = MockTables.NEPAL_13809326_Table_0;
    val sameTable = MockTables.NEPAL_13809326_Table_0;
    val diff = WordsExtractor.diffWords(table, sameTable);

    assertThat(diff).isEmpty();
  }

  @Test
  void extractAlphanumericWordsFromTableInLowercase() {
    val table = MockTables.NEPAL_13809326_Table_0;
    val words = WordsExtractor.extractWords(table);

    assertThat(words)
        .containsExactlyInAnyOrder("community", "district", "pop", "1991sup1sup", "pop", "2001",
            "average", "growthrate", "proj", "2005", "kathmandu", "kathmandu", "414264", "671846",
            "47", "807300", "lalitpur", "lalitpur", "117203", "162991", "34", "190900", "pokhara",
            "kaski", "95311", "156312", "50", "190900");
  }

  @Test
  void extractAlphanumericWordsFromStringInLowercase() {
    val text = "| Kathmandu || Kathmandu || 414.264 ||  671.846 ||  4,7 || 807.300";
    val words = WordsExtractor.extractWords(text);

    assertThat(words)
        .containsExactlyInAnyOrder("kathmandu", "kathmandu", "414264", "671846", "47", "807300");
  }
}

package util;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import mock.MockTables;
import org.junit.jupiter.api.Test;

class DiffUtilTest {

  @Test
  void diffReturnsWordsIntroducedInNewRevision() {
    val previousText = "| Kathmandu || Kathmandu || 414.264 ||  671.846 ||  4,7 || 807.300";
    val text = "| purple|| Kathmandu || 414.264 ||  671.846 ||  4,7 || 807.300";
    val diff = DiffUtil.diffWords(previousText, text);

    assertThat(diff).containsExactlyInAnyOrder("purple");
  }

  @Test
  void diffReturnsWordsFromNewRevisionIfPreviousIsEmpty() {
    val previousText = "";
    val text = "| purple|| Kathmandu || 414.264 ||  671.846 ||  4,7 || 807.300";
    val diff = DiffUtil.diffWords(previousText, text);

    assertThat(diff)
        .containsExactlyInAnyOrder("purple", "kathmandu", "414264", "671846", "47", "807300");
  }

  @Test
  void diffReturnsWordsInTableIntroducedInNewRevision() {
    val previousTable = MockTables.NEPAL_13809326_Table_0;
    val table = MockTables.NEPAL_13809566_Table_0;
    val diff = DiffUtil.diffWords(singleton(previousTable), singleton(table));

    assertThat(diff).containsExactlyInAnyOrder("purple", "duckie", "i", "am", "dumb");
  }

  @Test
  void diffReturnsNoWordsInTableWhenComparingTheSameRevision() {
    val table = MockTables.NEPAL_13809326_Table_0;
    val sameTable = MockTables.NEPAL_13809326_Table_0;
    val diff = DiffUtil.diffWords(singleton(table), singleton(sameTable));

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

  @Test
  void diffReturnsTokensInTableIntroducedInNewRevision() {
    val previousTable = MockTables.NEPAL_13809326_Table_0;
    val table = MockTables.NEPAL_13809326_Table_0_ADDED_SYNTAX;
    val diff = DiffUtil.diffTokens(singleton(previousTable), singleton(table));

    assertThat(diff).containsExactlyInAnyOrder("purple", "duckie", "i", "am", "dumb", "2001</b>", "[[Kathmandu]]", "<b>Pop.");
  }
}

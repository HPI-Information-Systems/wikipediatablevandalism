package features.context.impl;


import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import lombok.val;
import model.FeatureParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import wikixmlsplit.datastructures.MyRevisionType;

class AuthorRankTest {

  private static final String ANONYMOUS = null;
  private static final String BOB = "bob";
  private static final String ALICE = "alice";
  private static final String MALICE = "malice";

  private AuthorRank authorRank;

  @BeforeEach
  void setUp() {
    authorRank = new AuthorRank(RevisionProvider.all());
  }

  @Test
  void anonymous_hasLowestRankPossible() {
    val params = params(ANONYMOUS);

    val value = authorRank.getValue(params);

    assertThat(value).isEqualTo(-1);
  }

  @Test
  void firstEditOfCurrentUser_historyEmpty() {
    val params = params(BOB);

    val value = authorRank.getValue(params);

    assertThat(value).isZero();
  }

  @Test
  void firstEditOfCurrentUser_historyNotEmpty() {
    val params = params(BOB,
        ALICE);

    val value = authorRank.getValue(params);

    assertThat(value).isZero();
  }

  @Test
  void pageNotOwned() {
    val params = params(BOB,
        ALICE, ALICE, ALICE);

    val value = authorRank.getValue(params);

    assertThat(value).isZero();
  }

  @Test
  void pageOwned() {
    val params = params(BOB,
        BOB, ALICE, BOB);

    val value = authorRank.getValue(params);

    assertThat(value).isEqualTo(0.5);
  }

  @Test
  void someAuthorsOfSameRank() {
    val params = params(BOB,
        MALICE, MALICE, ALICE, ALICE, BOB, BOB, BOB, BOB, ANONYMOUS);

    val value = authorRank.getValue(params);

    assertThat(value).isEqualTo(0.5);
  }

  private FeatureParameters params(final String currentAuthor, final String... previousAuthors) {
    return FeatureParameters.builder()
        .revision(revisionOf(currentAuthor))
        .previousRevisions(Stream.of(previousAuthors).map(this::revisionOf).collect(toList()))
        .build();
  }

  private MyRevisionType revisionOf(final String author) {
    return new MyRevisionType() {{
      contributor = new ContributorType();
      contributor.setUsername(author);
    }};
  }

}

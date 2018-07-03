package features;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import model.FeatureContext;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.Settings;
import wikixmlsplit.api.TableMatcher;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

public class FeatureContextFactory {

  private final TableMatcher matcher = new TableMatcher(Settings.ofDefault());

  public FeatureContext create(final MyPageType page, final int revisionIndex) {
    return FeatureContext.builder()
        .page(page)
        .previousRevisions(previousRevisions(page, revisionIndex))
        .matching(getMatching(page))
        .build();
  }

  private List<MyRevisionType> previousRevisions(final MyPageType page, final int revisionIndex) {
    val n = 1; // TODO
    return page.getRevisions().subList(revisionIndex - n, revisionIndex);
  }

  private Matching getMatching(final MyPageType page) {
    return matcher.performMatching(page);
  }
}

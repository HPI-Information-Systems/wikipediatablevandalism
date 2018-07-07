package features;

import java.util.List;
import lombok.val;
import model.FeatureContext;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.Settings;
import wikixmlsplit.api.TableMatcher;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

public class FeatureContextFactory {

  public FeatureContext create(final MyPageType page, final int revisionIndex) {
    return FeatureContext.builder()
        .page(page)
        .previousRevisions(previousRevisions(page, revisionIndex))
        .matching(getMatching(page, revisionIndex))
        .build();
  }

  private List<MyRevisionType> previousRevisions(final MyPageType page, final int revisionIndex) {
    val n = 1;
    return page.getRevisions().subList(revisionIndex - n, revisionIndex);
  }

  private Matching getMatching(final MyPageType page, final int revisionIndex) {
    val matcher = new TableMatcher(Settings.ofDefault());
    return matcher.performMatching(page, revisionIndex);
  }
}

package tools;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import util.PageUtil;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Check if the difference between the two revisions contains changed tables on AST level.
 */
@Slf4j
public class RevisionChecker {

  private final TablePredicate tablePredicate = new TablePredicate();

  public boolean checkRevisionFeasible(final MyPageType page, final MyRevisionType revision) {
    final MyRevisionType previous = PageUtil.findPreviousRevision(page, revision);
    if (previous == null) {
      return true;
    }

    if (previous.getParsed() == null || revision.getParsed() == null) {
      return true;
    }

    final Collection<String> parsedDifference = CollectionUtils
        .disjunction(previous.getParsed(), revision.getParsed());
    return isTableInvolved(parsedDifference);
  }

  private boolean isTableInvolved(final Collection<String> parsed) {
    return parsed.stream().anyMatch(tablePredicate);
  }
}

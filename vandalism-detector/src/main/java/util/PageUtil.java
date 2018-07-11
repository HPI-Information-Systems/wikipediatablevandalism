package util;

import static java.util.stream.Collectors.toList;

import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.util.Collections;
import java.util.function.Supplier;
import lombok.val;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

public class PageUtil {

  public static MyRevisionType findRevision(final MyPageType page, final int revisionId) {
    val index = getRevisionIndex(page, revisionId);
    return page.getRevisions().get(index);
  }

  public static int getRevisionIndex(final MyPageType page, final MyRevisionType revision) {
    return getRevisionIndex(page, revision.getId());
  }

  public static int getRevisionIndex(final MyPageType page, final int revisionId) {
    return getRevisionIndex(page, BigInteger.valueOf(revisionId));
  }

  public static int getRevisionIndex(final MyPageType page, final BigInteger revisionId) {
    val revisionIds = page.getRevisions()
        .stream()
        .map(MyRevisionType::getId)
        .collect(toList());

    Preconditions.checkState(revisionIds.contains(revisionId), revisionNotFound(page, revisionId));
    return Collections.binarySearch(revisionIds, revisionId);
  }

  private static Supplier<String> revisionNotFound(final MyPageType page,
      final BigInteger revisionId) {
    return () -> String.format("Revision %s not found in page '%s' (%s)",
        revisionId, page.getTitle(), page.getId());
  }

}
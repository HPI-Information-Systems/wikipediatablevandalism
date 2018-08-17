package util;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.xml.datatype.DatatypeConstants;
import lombok.val;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import tools.TablePredicate;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class BasicUtils {

  /**
   * Check if return values of {@link javax.xml.datatype.XMLGregorianCalendar} are valid, else
   * throw.
   *
   * @param value the value to check
   * @return the valid value
   */
  public static int valid(final int value) {
    if (value == DatatypeConstants.FIELD_UNDEFINED) {
      throw new IllegalArgumentException("field undefined");
    }
    return value;
  }

  public static boolean isAnonymous(final ContributorType contributor) {
    return contributor.getUsername() == null;
  }

  public static int parsedLength(final List<String> parsed) {
    if (parsed == null) {
      return 0;
    }

    int totalLength = 0;
    for (String string : parsed) {
      if (TablePredicate.INSTANCE.test(string)) {
        totalLength += string.length();
      }
    }

    return totalLength;
  }

  public static MyRevisionType getPreviousRevision(final List<MyRevisionType> previousRevisions) {
    if (previousRevisions == null || previousRevisions.size() == 0) {
      return null;
    }
    return previousRevisions.get(0);
  }

  public static boolean hasSameContributor(final MyRevisionType revision1,
      final MyRevisionType revision2) {
    if (isAnonymous(revision1.getContributor()) &&
        isAnonymous(revision2.getContributor())) {
      return Objects.equals(revision1.getContributor().getIp(), revision2.getContributor().getIp());
    }

    return Objects.equals(revision1.getContributor().getId(), revision2.getContributor().getId());
  }

  @Nonnull
  public static List<WikiTable> getCurrentTables(final FeatureParameters parameters) {
    return getCurrentTables(parameters.getResult());
  }

  @Nonnull
  public static List<WikiTable> getCurrentTables(final TableMatchResult result) {
    final List<WikiTable> tables = new ArrayList<>(result.getAddedTables());
    for (final TableMatch match : result.getMatches()) {
      tables.add(match.getCurrentTable());
    }
    return tables;
  }

  @Nonnull
  public static List<WikiTable> getPreviousTables(final FeatureParameters parameters) {
    return getPreviousTables(parameters.getResult());
  }

  @Nonnull
  public static List<WikiTable> getPreviousTables(final TableMatchResult result) {
    final List<WikiTable> tables = new ArrayList<>(result.getRemovedTables());
    for (final TableMatch match : result.getMatches()) {
      tables.add(match.getPreviousTable());
    }
    return tables;
  }

  public static long getTimeDuration(final MyRevisionType currentRevision,
      final MyRevisionType previousRevision) {
    val currentRevisionTime = currentRevision.getDate().toInstant();
    val previousRevisionTime = previousRevision.getDate().toInstant();
    Preconditions.checkState(!previousRevisionTime.isAfter(currentRevisionTime),
        "previousRevisionTime should be before revisionTime");
    return Duration.between(previousRevisionTime, currentRevisionTime)
        .toMinutes();
  }
}

package util;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.DatatypeConstants;
import lombok.val;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
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

  @Nullable
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

  /**
   * @return the current state of all tables which underwent modification in this edit
   */
  @Nonnull
  public static List<WikiTable> getCurrentChangedTables(final FeatureParameters parameters) {
    final List<WikiTable> changed = new ArrayList<>();
    for (final TableMatch match : parameters.getChangedTables()) {
      changed.add(match.getCurrentTable());
    }
    return changed;
  }

  /**
   * @return the previous state of all tables which underwent modification in this edit
   */
  @Nonnull
  public static List<WikiTable> getPreviousChangedTables(final FeatureParameters parameters) {
    final List<WikiTable> changed = new ArrayList<>();
    for (final TableMatch match : parameters.getChangedTables()) {
      changed.add(match.getPreviousTable());
    }
    return changed;
  }

  /**
   * @return all tables which are currently visible on the page, including edited and added tables
   */
  @Nonnull
  public static List<WikiTable> getCurrentTables(final FeatureParameters parameters) {
    return getCurrentTables(parameters.getResult());
  }

  /**
   * @return all tables which are currently visible on the page, including edited and added tables
   */
  @Nonnull
  public static List<WikiTable> getCurrentTables(final TableMatchResult result) {
    final List<WikiTable> tables = new ArrayList<>(result.getAddedTables());
    for (final TableMatch match : result.getMatches()) {
      tables.add(match.getCurrentTable());
    }
    return tables;
  }

  /**
   * @return all tables of the previous revision, including prior states of edited tables and the
   * ones that are now deleted
   */
  @Nonnull
  public static List<WikiTable> getPreviousTables(final FeatureParameters parameters) {
    return getPreviousTables(parameters.getResult());
  }

  /**
   * @return all tables of the previous revision, including prior states of edited tables and the
   * ones that are now deleted
   */
  @Nonnull
  public static List<WikiTable> getPreviousTables(final TableMatchResult result) {
    final List<WikiTable> tables = new ArrayList<>(result.getRemovedTables());
    for (final TableMatch match : result.getMatches()) {
      tables.add(match.getPreviousTable());
    }
    return tables;
  }

  /**
   * @return the minutes in between to revisions
   */
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

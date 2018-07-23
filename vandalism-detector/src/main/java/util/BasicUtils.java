package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.xml.datatype.DatatypeConstants;
import lombok.val;
import matching.table.TableMatch;
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

  public static int parsedLength(final List<String> parsed) {
    if (parsed == null) {
      return 0;
    }
    int totalLength = 0;
    for (String string : parsed) {
      totalLength += string.length();
    }
    return totalLength;
  }

  public static MyRevisionType getPreviousRevision(final List<MyRevisionType> previousRevisions) {
    if (previousRevisions == null || previousRevisions.size() < 2) { // == 0
      return null;
    }
    return previousRevisions.get(1); // 0
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
    val matchingResult = parameters.getResult();
    final List<WikiTable> tables = new ArrayList<>(matchingResult.getAddedTables());
    for (final TableMatch match : matchingResult.getMatches()) {
      tables.add(match.getCurrentTable());
    }
    return tables;
  }

  @Nonnull
  public static List<WikiTable> getPreviousTables(final FeatureParameters parameters) {
    val matchingResult = parameters.getResult();
    final List<WikiTable> tables = new ArrayList<>(matchingResult.getRemovedTables());
    for (final TableMatch match : matchingResult.getMatches()) {
      tables.add(match.getPreviousTable());
    }
    return tables;
  }
}

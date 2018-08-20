package features;

import static util.PageUtil.getRevisionIndex;

import features.content.util.TableContentExtractor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import matching.row.RowMatchResult;
import matching.row.RowMatchService;
import matching.table.TableMatch;
import matching.table.TableMatchResult;
import matching.table.TableMatchService;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.CommentType;
import util.BasicUtils;
import util.CommentPreprocessor;
import util.PageUtil;
import wikixmlsplit.api.Matching;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

@Slf4j
class FeatureParametersFactory {

  FeatureParameters create(final MyPageType page, final MyRevisionType revision,
      final Matching matching) {

    final TableMatchResult tableMatchResult = getTableMatching(page, revision, matching);
    final List<TableMatch> changedTables = changedTables(tableMatchResult);
    final TableMatch selectedMatch = changedTables.isEmpty() ? null : changedTables.get(0);
    final List<MyRevisionType> previousRevisions = previousRevisions(page, revision);
    final String userComment = extractUserComment(revision.getComment());

    final List<WikiTable> currentTables = BasicUtils.getCurrentTables(tableMatchResult);
    final List<WikiTable> previousTables = BasicUtils.getPreviousTables(tableMatchResult);

    return FeatureParameters.builder()
        // Meta
        .page(page)
        .revision(revision)
        .previousRevision(BasicUtils.getPreviousRevision(previousRevisions))
        .previousRevisions(previousRevisions)

        // Matching
        .matching(matching)
        .result(tableMatchResult)
        .relevantMatch(selectedMatch)
        .rowMatchResult(getRowMatching(selectedMatch))
        .matchedTables(tableMatchResult.getMatches())
        .changedTables(changedTables)

        // Comments
        .userComment(userComment)
        .rawComment(absentCommentToEmptyString(revision.getComment()))

        // Content
        .contentWithComment(TableContentExtractor.getContentWithComment(userComment, currentTables))
        .content(TableContentExtractor.getContent(currentTables))
        .previousContent(TableContentExtractor.getContent(previousTables))

        .build();
  }

  private List<MyRevisionType> previousRevisions(final MyPageType page,
      final MyRevisionType revision) {

    val revisionIndex = getRevisionIndex(page, revision);
    final List<MyRevisionType> revisions = new ArrayList<>(
        page.getRevisions().subList(0, revisionIndex));

    // reverse list -> index = 0 is the previous revision, index = 1 is the one before, etc.
    Collections.reverse(revisions);
    return revisions;
  }

  private List<TableMatch> changedTables(final TableMatchResult result) {
    final List<TableMatch> changed = new ArrayList<>();
    for (final TableMatch match : result.getMatches()) {
      // TODO consider similarity < 1
      if (!match.getPreviousTable().equals(match.getCurrentTable())) {
        changed.add(match);
      }
    }

    if (changed.isEmpty()) {
      log.warn("All matched tables identical");
    }
    return changed;
  }

  private TableMatchResult getTableMatching(final MyPageType page, final MyRevisionType revision,
      final Matching matching) {
    val tableMatchService = new TableMatchService();
    val previousRevision = PageUtil.findPreviousRevision(page, revision);
    return tableMatchService.getMatchingTable(matching, revision, previousRevision);
  }

  private RowMatchResult getRowMatching(final TableMatch match) {
    val rowMatchService = new RowMatchService();
    return match == null ? null :
        rowMatchService.matchRows(match.getPreviousTable(), match.getCurrentTable());
  }

  private String extractUserComment(@Nullable final CommentType comment) {
    return CommentPreprocessor.extractUserComment(comment);
  }

  private String absentCommentToEmptyString(@Nullable final CommentType comment) {
    if (CommentPreprocessor.isBlank(comment)) {
      return "";
    }
    return comment.getValue();
  }
}

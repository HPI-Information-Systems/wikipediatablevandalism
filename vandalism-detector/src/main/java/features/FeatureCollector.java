package features;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static util.PerformanceUtil.runMeasured;

import features.output.Output;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import matching.persistence.MatchingFacade;
import model.RevisionTag;
import model.Tag;
import runner.Arguments;
import tools.RevisionChecker;
import util.PageUtil;
import wikixmlsplit.api.Matching;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Compute features for a given revision and forward the results to the sink.
 */
@Slf4j
public class FeatureCollector {

  private final RevisionChecker revisionChecker;
  private final FeaturePack pack;
  private final FeatureSink sink;
  private final FeatureParametersFactory contextFactory;
  private final MatchingFacade matchingFacade;

  public FeatureCollector(final Arguments arguments, final FeaturePack pack, final Output output) {
    revisionChecker = new RevisionChecker();
    matchingFacade = new MatchingFacade(arguments);
    this.pack = pack;
    sink = new FeatureSink(pack, output);
    sink.setup();
    contextFactory = new FeatureParametersFactory();
  }

  public synchronized void accept(final MyPageType page, final List<RevisionTag> observations) {

    final Map<Integer, List<RevisionTag>> groupByRevision = observations.stream()
        .collect(groupingBy(r -> r.getPageRevision().getRevisionId()));

    val maxRevisionId = Collections.max(groupByRevision.keySet());
    val matching = matchingFacade.obtainMatching(page, maxRevisionId);

    for (val revisionIdWithTags : groupByRevision.entrySet()) {
      final List<Tag> tags = revisionIdWithTags.getValue().stream()
          .filter(revisionTag -> revisionTag.getTag() != null)
          .map(RevisionTag::getTag)
          .collect(toList());
      
      try {
        final MyRevisionType revision = PageUtil.findRevision(page, revisionIdWithTags.getKey());
        accept(tags, page, revision, matching);
      } catch (final Exception e) {
        log.error("Error during feature computation on revision {} of page {} (\"{}\")",
            revisionIdWithTags.getKey(), page.getId(), page.getTitle(), e);
      }
    }

    WikiTable.resetBagOfWordsCache();
  }

  private void accept(final List<Tag> tags, final MyPageType page, final MyRevisionType revision,
      final Matching matching) {

    log.debug("Processing revision {} of page {} ({} {})",
        revision.getId(), page.getId(), tags.size(), tags.size() == 1 ? "tag" : "tags");

    if (skipRevision(page, revision)) {
      return;
    }

    val values = new HashMap<String, Object>();
    val featureContext = contextFactory.create(page, revision, matching);

    runMeasured("Feature computation", () -> pack.forEachFeature(
        (name, feature) -> values.put(name, feature.getValue(featureContext))));

    sink.accept(revision, tags, values);
  }

  private boolean skipRevision(final MyPageType page, final MyRevisionType revision) {
    final boolean valid = revisionChecker.checkRevisionFeasible(page, revision);
    if (!valid) {
      log.warn("Revision {} of page {} (\"{}\") is SKIPPED",
          revision.getId(), page.getId(), page.getTitle());
    }
    return !valid;
  }
}

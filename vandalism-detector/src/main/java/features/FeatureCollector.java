package features;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static util.PerformanceUtil.runMeasured;

import features.output.Output;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.val;
import model.RevisionTag;
import model.Tag;
import util.PageUtil;
import wikixmlsplit.api.Matching;
import wikixmlsplit.api.Settings;
import wikixmlsplit.api.TableMatcher;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;
import wikixmlsplit.renderer.wikitable.WikiTable;

/**
 * Compute features for a given revision and forward the results to the sink.
 */
public class FeatureCollector {

  private final FeaturePack pack;
  private final FeatureSink sink;
  private final FeatureContextFactory contextFactory;

  public FeatureCollector(final FeaturePack pack, final Output output) {
    this.pack = pack;
    sink = new FeatureSink(pack, output);
    sink.setup();
    contextFactory = new FeatureContextFactory();
  }

  public void accept(final MyPageType page, final List<RevisionTag> observations) {

    final Map<Integer, List<RevisionTag>> groupByRevision = observations.stream()
        .collect(groupingBy(r -> r.getPageRevision().getRevisionId()));

    val maxRevisionId = Collections.max(groupByRevision.keySet());
    val matching = getMatching(page, maxRevisionId);

    for (val revisionIdWithTags : groupByRevision.entrySet()) {
      final List<Tag> tags = revisionIdWithTags.getValue().stream().map(RevisionTag::getTag)
          .collect(toList());
      final MyRevisionType revision = PageUtil.findRevision(page, revisionIdWithTags.getKey());
      accept(tags, page, revision, matching);
    }

    WikiTable.resetBagOfWordsCache();
  }

  private Matching getMatching(final MyPageType page, final int maxRevisionId) {
    val matcher = new TableMatcher(Settings.ofDefault());
    val revisionIndex = PageUtil.getRevisionIndex(page, maxRevisionId);
    return runMeasured("Page matching", () -> matcher.performMatching(page, revisionIndex));
  }

  private void accept(final List<Tag> tags, final MyPageType page, final MyRevisionType revision,
      final Matching matching) {

    val values = new HashMap<String, Object>();
    val featureContext = contextFactory.create(page, revision, matching);

    runMeasured("Feature computation", () -> pack.forEachFeature(
        (name, feature) -> values.put(name, feature.getValue(revision, featureContext))));

    sink.accept(tags, values);
  }
}

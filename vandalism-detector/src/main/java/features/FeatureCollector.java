package features;

import static util.PerformanceUtil.runMeasured;

import features.output.Output;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import model.Tag;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

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

  public void accept(final List<Tag> tags, final BigInteger revisionId, final MyPageType page) {
    val revisionIDs = page.getRevisions()
        .stream()
        .map(MyRevisionType::getId)
        .collect(Collectors.toList());

    val revisionIndex = Collections.binarySearch(revisionIDs, revisionId);
    val revision = page.getRevisions().get(revisionIndex);

    val values = new HashMap<String, Object>();
    val featureContext = contextFactory.create(page, revisionIndex);

    runMeasured("Feature computation", () -> pack.forEachFeature(
        (name, feature) -> values.put(name, feature.getValue(revision, featureContext))));

    sink.accept(tags, values);
  }
}

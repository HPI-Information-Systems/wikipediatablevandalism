package features;

import features.output.Output;
import java.util.HashMap;
import java.util.List;
import lombok.val;
import model.Tag;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Compute features for a given revision and forward the results to the sink.
 */
public class FeatureCollector {

  private final FeaturePack pack;
  private final FeatureSink sink;

  public FeatureCollector(final FeaturePack pack, final Output output) {
    this.pack = pack;
    sink = new FeatureSink(pack, output);
    sink.setup();
  }

  public void accept(final List<Tag> tags, final MyRevisionType revisionType) {
    val values = new HashMap<String, Object>();
    pack.forEachFeature((name, feature) -> {
      values.put(name, feature.getValue(revisionType));
    });

    sink.accept(tags, values);
  }
}

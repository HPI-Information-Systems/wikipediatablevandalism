package features;

import com.google.common.collect.ImmutableList;
import features.context.ContextFeatures;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.RevisionTag;
import model.Tag;
import wikixmlsplit.datastructures.MyRevisionType;

@RequiredArgsConstructor
public class FeatureCollector {

  private final ContextFeatures context = new ContextFeatures();
  private final FeatureSink sink;

  public void collectFeatures(final List<Tag> tags, final MyRevisionType revisionType) {
    val values = new HashMap<String, Object>();
    for(val feature : context.features().entrySet()) {
      values.put(feature.getKey(), feature.getValue().getValue(revisionType));
    }
    sink.accept(tags, values);
  }

}

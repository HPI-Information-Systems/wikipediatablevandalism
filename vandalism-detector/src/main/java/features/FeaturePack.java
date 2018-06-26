package features;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * An immutable collection of features. The most important property is the stable iteration order of
 * feature names.
 */
@Data
public class FeaturePack {

  private final Map<String, Feature> features;

  @Builder
  public FeaturePack(@Singular final Map<String, Feature> features) {
    this.features = ImmutableMap.copyOf(features);
  }

  public List<String> getNames() {
    return ImmutableList.copyOf(features.keySet());
  }

  public void forEachFeature(final BiConsumer<String, Feature> consumer) {
    features.forEach(consumer);
  }

  public FeaturePack combineWith(final FeaturePack other) {
    return FeaturePack.builder()
        .features(features)
        .features(other.features)
        .build();
  }
}

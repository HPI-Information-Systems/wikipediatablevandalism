package features;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Preconditions;
import features.output.Output;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.Tag;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Feed results of the feature computation to the sink.
 *
 * <p>Strategy: add a single field "tag_id" to the output. For each of the tags, output one
 * instance of the record with the associated tag.</p>
 */
@RequiredArgsConstructor
class FeatureSink {

  private static final String FIELD_TAG = "tag_id";
  private static final String FIELD_REVISION_ID = "revision_id";

  private final FeaturePack pack;
  private final Output output;

  void setup() {
    final List<String> header = new ArrayList<>(pack.getNames());
    header.add(FIELD_REVISION_ID);
    header.add(FIELD_TAG);
    output.setup(header);
  }

  void accept(final MyRevisionType revision, final List<Tag> tags,
      final Map<String, Object> featureMapping) {
    Preconditions.checkState(!tags.isEmpty(), "Empty tags");

    final List<Object> featureValues = new ArrayList<>(featureMapping.size());
    for (final String name : pack.getNames()) {
      val value = featureMapping.get(name);
      requireNonNull(value,
          () -> String.format("Feature %s has no value in %s", name, featureMapping));
      featureValues.add(value);
    }

    for (Tag tag : tags) {
      List<Object> record = new ArrayList<>(featureValues.size() + 2);
      record.addAll(featureValues);
      record.add(revision.getId());
      record.add(tag.getTagId());
      output.accept(record);
    }
  }
}

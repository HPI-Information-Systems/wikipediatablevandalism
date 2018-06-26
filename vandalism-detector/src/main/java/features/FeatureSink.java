package features;

import static java.util.Objects.requireNonNull;

import features.output.Output;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.Tag;

/**
 * Feed results of the feature computation to the sink.
 *
 * <p>Strategy: add a single field "tag_id" to the output. For each of the tags, output one
 * instance of the record with the associated tag.</p>
 */
@RequiredArgsConstructor
class FeatureSink {

  private static final String FIELD_TAG = "tag_id";

  private final FeaturePack pack;
  private final Output output;

  void setup() {
    final List<String> header = new ArrayList<>(pack.getNames());
    header.add(FIELD_TAG);
    output.setup(header);
  }

  void accept(final List<Tag> tags, final Map<String, Object> values) {
    final List<Object> toWrite = new ArrayList<>(values.size() + 1);
    for (final String name : pack.getNames()) {
      val value = values.get(name);
      requireNonNull(value, () -> String.format("Feature %s has no value in %s", name, values));
      toWrite.add(value);
    }

    val lastIndex = toWrite.size() - 1;
    for (val tag : tags) {
      toWrite.set(lastIndex, tag.getTagId());
      output.accept(toWrite);
    }
  }
}

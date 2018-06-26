package features;

import com.google.common.collect.Ordering;
import features.context.ContextFeatures;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.val;
import model.Tag;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class FeatureSink implements AutoCloseable {

  private static final String TAG = "tag";

  private final String outputFilename;
  private final String[] header;
  private CSVPrinter printer;

  public FeatureSink(final String outputFilename, final ContextFeatures contextFeatures) {
    this.outputFilename = outputFilename;
    List<String> names = new ArrayList<>(contextFeatures.features().keySet());
    names.sort(Ordering.natural());
    names.add(TAG);
    header = names.toArray(new String[names.size()]);
  }

  public void begin() {
    try {
      val writer = Files.newBufferedWriter(Paths.get(outputFilename));
      printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(header));
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void accept(final List<Tag> tags, final Map<String, Object> values) {
    List<Entry<String, Object>> entries = new ArrayList<>(values.entrySet());
    entries.sort(Comparator.comparing(Map.Entry::getKey));

    List<Object> toWrite = new ArrayList<>(entries.size() + 1);
    for (Entry<String, Object> entry : entries) {
      toWrite.add(entry.getValue());
    }

    toWrite.add("");

    try {
      for (final Tag t : tags) {
        toWrite.set(toWrite.size() - 1, t.getTagId());
        printer.printRecord(toWrite);
      }
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void close() throws IOException {
    printer.close();
  }
}

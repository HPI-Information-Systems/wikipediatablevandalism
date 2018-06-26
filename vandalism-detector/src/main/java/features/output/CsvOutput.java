package features.output;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Feature persistence based on Apache Commons CSV.
 */
@RequiredArgsConstructor
class CsvOutput implements Output {

  private final Path outputPath;

  private CSVPrinter printer;

  @Override
  public void setup(final List<String> header) {
    try {
      final String[] headerArray = header.toArray(new String[header.size()]);
      val writer = Files.newBufferedWriter(outputPath);
      printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headerArray));
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void accept(final List<Object> record) {
    requireNonNull(printer, "call setup() first");

    try {
      for (val item : record) {
        if (item instanceof Boolean) {
          val intValue = ((boolean) item) ? 1 : 0;
          printer.print(intValue);
        } else {
          printer.print(item);
        }
      }
      printer.println();
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void close() {
    try {
      printer.close();
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}

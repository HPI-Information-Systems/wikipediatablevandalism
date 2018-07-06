package features.output;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Feature persistence based on Apache Commons CSV.
 */
@Slf4j
@RequiredArgsConstructor
class CsvOutput implements Output {

  private final Path outputPath;

  private CSVPrinter printer;

  @Override
  public void setup(final List<String> header) {
    log.info("Writing to {}", outputPath.toAbsolutePath());

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
      val mappedRecord = record.stream().map(value -> {
        if (value instanceof Boolean) {
          return (boolean) value ? 1 : 0;
        }
        return value;
      }).collect(Collectors.toList());
      printer.printRecord(mappedRecord);
      printer.flush();
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

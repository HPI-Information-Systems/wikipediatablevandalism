package features.output;

import java.nio.file.Path;
import java.util.List;

/**
 * Persistence of computed feature values.
 */
public interface Output extends AutoCloseable {

  void setup(List<String> header);

  void accept(List<Object> record);

  @Override
  void close();

  static Output csv(final Path path) {
    return new CsvOutput(path);
  }
}

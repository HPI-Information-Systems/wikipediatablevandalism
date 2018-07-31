package runner;

import static java.util.Objects.requireNonNull;

import com.beust.jcommander.Parameter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Arguments {

  static final int INVALID_TAG_ID = -1;
  static final int NO_LIMIT = -1;

  @Parameter(names = "--tags", description = "File containing tagged observations: (page, revision, tag). Default value: mini deleted sample.")
  Path revisionTagPath = getDefaultRevisionTagPath();

  @Parameter(names = "--revision-path", description = "Path to .parsed files containing the revisions of a page")
  Path revisionPath;

  @Parameter(names = "--output", description = "Path of the output CSV to write")
  Path outputPath;

  @Parameter(names = "--limit", description = "Inspect at most n revision / tag instances")
  int observationLimit = NO_LIMIT;

  @Parameter(names = "--only-tag", description = "Inspect only observation with given tag ID (N instances) plus N counter-instances")
  int tagId = INVALID_TAG_ID;

  @Parameter(names = "--matching-path", description = "Path to matching persistence")
  Path matchingPath;

  @Parameter(names = "--parallel", description = "Number of parallel threads")
  int parallel = Runtime.getRuntime().availableProcessors();

  @Parameter(names = {"-h", "--help"}, help = true)
  boolean help;

  private static Path getDefaultRevisionTagPath() {
    final URL url = requireNonNull(
        Arguments.class.getClassLoader().getResource("mini_deleted_sample.csv"),
        "Default revision tag not found");
    return Paths.get(url.getPath());
  }
}

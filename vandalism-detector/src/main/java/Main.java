import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.esotericsoftware.kryo.Kryo;
import features.FeatureCollector;
import features.basic.BasicFeatures;
import features.content.ContentFeatures;
import features.context.ContextFeatures;
import features.future.FutureFeatures;
import features.output.Output;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import model.PageRevision;
import parser.PageParser;
import parser.PagePathFinder;
import parser.RevisionTagParser;

@Slf4j
public class Main {

  private void importDataSet(final Arguments arguments) {
    try {
      val revisionTagPath = getRevisionTagPath(arguments);
      val revisionTagParser = new RevisionTagParser();
      val revisionTags = revisionTagParser.load(revisionTagPath);
      val pagePathFinder = new PagePathFinder(arguments.getRevisionPath());
      val pageIds = revisionTags.keySet()
          .stream()
          .map(PageRevision::getPageId)
          .distinct()
          .collect(Collectors.toList());
      val pagePaths = pagePathFinder.findAll(pageIds);
      val pageParser = new PageParser(new Kryo());

      try (val output = Output.csv(arguments.getOutputPath())) {
        val pack = BasicFeatures.get().getFeatures()
            .combineWith(ContextFeatures.get().getFeatures())
            .combineWith(ContentFeatures.get().getFeatures())
            .combineWith(FutureFeatures.get().getFeatures());

        val collector = new FeatureCollector(pack, output);

        for (PageRevision pageRevision : revisionTags.keySet()) {
          val path = pagePaths.get(pageRevision.getPageId());
          val page = pageParser.parse(path);
          log.debug(page.getTitle());

          collector.accept(revisionTags.get(pageRevision),
              BigInteger.valueOf(pageRevision.getRevisionId()), page);

        }
      }
    } catch (IOException e) {
      log.error("Error from shutting down output", e);
    }
  }

  private String getRevisionTagPath(final Arguments args) {
    if (args.getRevisionTagPath() == null) {
      return getClass().getResource("mini_deleted_sample.csv").getPath();
    }
    return args.getRevisionTagPath().toString();
  }

  public static void main(String[] args) {
    val arguments = new Arguments();
    val jcommander = JCommander.newBuilder().addObject(arguments).build();
    jcommander.parse(args);

    Main main = new Main();
    main.importDataSet(arguments);
  }

  @Getter
  public static class Arguments {

    @Parameter(names = "--tags")
    Path revisionTagPath;

    @Parameter(names = "--revision-path")
    Path revisionPath;

    @Parameter(names = "--output")
    Path outputPath;
  }
}

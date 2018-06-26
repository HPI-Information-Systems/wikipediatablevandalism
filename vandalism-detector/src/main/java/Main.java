import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.esotericsoftware.kryo.Kryo;
import features.FeatureCollector;
import features.context.ContextFeatures;
import features.output.Output;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import model.PageRevision;
import parser.PageParser;
import parser.PagePathFinder;
import parser.RevisionTagParser;

@Log4j2
public class Main {

  public void importDataSet(final Arguments arguments) {
    try {
      val revisionTagPath = getClass().getResource("revisiontag_deleted_1k.csv").getPath();
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
        val pack = ContextFeatures.get().getFeatures();
        val collector = new FeatureCollector(pack, output);

        for (PageRevision pageRevision : revisionTags.keySet()) {
          val path = pagePaths.get(pageRevision.getPageId());
          val page = pageParser.parse(path);

          val revision = page.getRevisions().stream()
              .filter(r -> r.getId().equals(BigInteger.valueOf(pageRevision.getRevisionId())))
              .findFirst()
              .orElseThrow(IllegalArgumentException::new);

          collector.accept(revisionTags.get(pageRevision), revision);

          log.debug(page.getTitle());
        }
      }
    } catch (IOException e) {
      log.error(e);
    }

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

    @Parameter(names = {"--revision-path"})
    Path revisionPath;

    @Parameter(names = {"--output"})
    Path outputPath;
  }
}

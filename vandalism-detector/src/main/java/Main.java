import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import com.beust.jcommander.JCommander;
import com.esotericsoftware.kryo.Kryo;
import features.FeatureCollector;
import features.FeaturePack;
import features.basic.BasicFeatures;
import features.content.ContentFeatures;
import features.context.ContextFeatures;
import features.future.FutureFeatures;
import features.language.LanguageFeatures;
import features.output.Output;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import model.RevisionTag;
import parser.PageParser;
import parser.PagePathFinder;
import runner.Arguments;
import runner.ObservationCollector;
import wikixmlsplit.datastructures.MyPageType;

@Slf4j
public class Main {

  private static final DecimalFormat PERCENT = new DecimalFormat("0.00 %");


  private final Arguments arguments;
  private final PagePathFinder finder;
  private final PageParser parser;
  private final ObservationCollector observationCollector;

  private Main(final Arguments arguments) {
    this.arguments = arguments;
    finder = new PagePathFinder(arguments.getRevisionPath());
    parser = new PageParser(new Kryo());
    observationCollector = new ObservationCollector(arguments);
  }

  private Map<Integer, Path> findPages(final List<RevisionTag> observations) {
    val pageIds = observations.stream().map(r -> r.getPageRevision().getPageId()).collect(toSet());
    return finder.findAll(pageIds);
  }

  private MyPageType loadPage(final Map<Integer, Path> pageIdToPath, final int pageId) {
    val pagePath = requireNonNull(pageIdToPath.get(pageId), "Page file of " + pageId + "not found");
    return parser.parse(pagePath);
  }

  private void run(final FeatureCollector collector) {
    final List<RevisionTag> observations = observationCollector.collectObservations();
    final Map<Integer, List<RevisionTag>> pageToObservations =
        observations.stream().collect(groupingBy(t -> t.getPageRevision().getPageId()));
    final Map<Integer, Path> pageIdToPath = findPages(observations);

    int processed = 0;
    final int total = pageToObservations.size();
    for (val entry : pageToObservations.entrySet()) {
      val page = loadPage(pageIdToPath, entry.getKey());
      logProgress(page, processed, total);
      collector.accept(page, entry.getValue());
      ++processed;
    }
  }

  private void logProgress(final MyPageType page, final int processed, final int total) {
    final double percent = (double) processed / total;
    log.info("Processing page {} ({} / {}, {})",
        page.getTitle(), processed, total, PERCENT.format(percent));
  }

  private void runPack(final FeaturePack pack) {
    try (val output = Output.csv(arguments.getOutputPath())) {
      val collector = new FeatureCollector(arguments, pack, output);
      run(collector);
    }
  }

  private static FeaturePack getDefaultFeaturePack() {
    return BasicFeatures.get().getFeatures()
        .combineWith(ContextFeatures.get().getFeatures())
        .combineWith(ContentFeatures.get().getFeatures())
        .combineWith(LanguageFeatures.get().getFeatures())
        .combineWith(FutureFeatures.get().getFeatures());
  }


  public static void main(final String[] args) {
    val arguments = new Arguments();
    val jcommander = JCommander.newBuilder().addObject(arguments).build();
    jcommander.parse(args);

    log.trace("Arguments: {}", arguments);

    if (arguments.isHelp()) {
      jcommander.usage();
      return;
    }

    Main main = new Main(arguments);
    main.runPack(getDefaultFeaturePack());
  }

}

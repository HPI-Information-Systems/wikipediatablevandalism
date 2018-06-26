import com.esotericsoftware.kryo.Kryo;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import model.PageRevision;
import parser.PageParser;
import parser.PagePathFinder;
import parser.RevisionTagParser;

import java.io.IOException;
import java.util.stream.Collectors;

@Log4j2
public class Main {
    public void importDataSet() {
        try {
            val revisionPath = "/Volumes/Calcutec/vandalism-detection/corpus/corpus/";
            val revisionTagPath = getClass().getResource("revisiontag_deleted_1k.csv").getPath();
            val revisionTagParser = new RevisionTagParser();
            val revisionTags = revisionTagParser.load(revisionTagPath);
            val pagePathFinder = new PagePathFinder(revisionPath);
            val pageIds = revisionTags.keySet()
                    .stream()
                    .map(PageRevision::getPageId)
                    .distinct()
                    .collect(Collectors.toList());
            val pagePaths = pagePathFinder.findAll(pageIds);
            val pageParser = new PageParser(new Kryo());

            for (PageRevision pageRevision : revisionTags.keySet()) {
                val path = pagePaths.get(pageRevision.getPageId());
                val page = pageParser.parse(path);
                log.debug(page.getTitle());
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.importDataSet();
    }
}

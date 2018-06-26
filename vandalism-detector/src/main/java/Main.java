import lombok.extern.log4j.Log4j2;
import lombok.val;
import model.RevisionTag;
import parser.RevisionParser;
import parser.RevisionTagParser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class Main {
    public void importDataSet() {
        try {
            val revisionPath = "";
            val revisionTagPath = getClass().getResource("revisiontag_deleted_1k.csv").getPath();
            val revisionTagParser = new RevisionTagParser();
            val revisionTags = revisionTagParser.load(revisionTagPath);
            val revisionParser = new RevisionParser(revisionPath);
            val pageIds = revisionTags.values()
                    .stream()
                    .flatMap(List::stream)
                    .map(RevisionTag::getRevisionPageId)
                    .distinct()
                    .collect(Collectors.toList());
            val pagePaths = revisionParser.parse(pageIds);

            log.debug(pagePaths);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.importDataSet();
    }
}

import lombok.extern.log4j.Log4j2;
import model.RevisionTag;

import java.io.IOException;
import java.util.List;

@Log4j2
public class Main {
    public void importDataSet() {
        try {
            String revisionTagPath = getClass().getResource("revisiontag_deleted_1k.csv").getPath();
            RevisionTagParser revisionTagParser = new RevisionTagParser();
            List<RevisionTag> revisionTags = revisionTagParser.load(revisionTagPath);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.importDataSet();
    }
}

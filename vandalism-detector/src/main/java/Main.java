import lombok.extern.log4j.Log4j2;
import lombok.val;
import model.RevisionTag;
import wikixmlsplit.datastructures.MyPageType;

import java.io.IOException;
import java.util.List;

@Log4j2
public class Main {
    public void importDataSet() {
        try {
            val revisionTagPath = getClass().getResource("revisiontag_deleted_1k.csv").getPath();
            val revisionTagParser = new RevisionTagParser();
            val revisionTags = revisionTagParser.load(revisionTagPath);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.importDataSet();
    }
}

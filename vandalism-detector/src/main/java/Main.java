import lombok.extern.log4j.Log4j2;
import smile.data.AttributeDataset;

import java.io.IOException;
import java.text.ParseException;

@Log4j2
public class Main {
    public void importDataSet() {
        RevisionTagParser parser = new RevisionTagParser();

        try {
            String revisionTagPath = getClass().getResource("revisiontag.csv").getPath();
            AttributeDataset dataset = parser.read(revisionTagPath);
            log.debug("Hello");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.importDataSet();
    }
}

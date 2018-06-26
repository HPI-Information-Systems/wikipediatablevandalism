import model.RevisionTag;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RevisionTagParser {
    public List<RevisionTag> load(String path) throws IOException {
        Reader in = new FileReader(path);
        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(in);

        return StreamSupport.stream(parser.spliterator(), false)
                .map(record -> {
                    int revisionId = Integer.valueOf(record.get(0));
                    int revisionPageId = Integer.valueOf(record.get(1));
                    int tagId = Integer.valueOf(record.get(2));
                    return new RevisionTag(revisionId, revisionPageId, tagId);
                }).collect(Collectors.toList());
    }
}

package parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import model.PageRevision;
import model.RevisionTag;
import model.Tag;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class RevisionTagParser {

  public List<RevisionTag> load(Path path) throws IOException {
    Reader in = new FileReader(path.toFile());
    CSVParser parser = CSVFormat.DEFAULT
        .withFirstRecordAsHeader()
        .parse(in);

    final List<RevisionTag> pageRevisions = new ArrayList<>();
    for (final CSVRecord record : parser) {
      int revisionId = Integer.valueOf(record.get(0));
      int revisionPageId = Integer.valueOf(record.get(1));
      int tagId = Integer.valueOf(record.get(2));
      val pageRevision = PageRevision.builder()
          .revisionId(revisionId)
          .pageId(revisionPageId)
          .build();
      val tag = new Tag(tagId);
      pageRevisions.add(new RevisionTag(pageRevision, tag));

    }
    return pageRevisions;
  }
}

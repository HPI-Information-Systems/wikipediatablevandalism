package parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.val;
import model.PageRevision;
import model.RevisionTag;
import model.Tag;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class RevisionTagParser {

  public Map<PageRevision, List<Tag>> load(String path) throws IOException {
    Reader in = new FileReader(path);
    CSVParser parser = CSVFormat.DEFAULT
        .withFirstRecordAsHeader()
        .parse(in);

    return StreamSupport.stream(parser.spliterator(), false)
        .map(record -> {
          int revisionId = Integer.valueOf(record.get(0));
          int revisionPageId = Integer.valueOf(record.get(1));
          int tagId = Integer.valueOf(record.get(2));
          val pageRevision = PageRevision.builder()
              .revisionId(revisionId)
              .pageId(revisionPageId)
              .build();
          val tag = new Tag(tagId);
          return new RevisionTag(pageRevision, tag);
        }).collect(Collectors.groupingBy(
            RevisionTag::getPageRevision,
            Collectors.mapping(RevisionTag::getTag, Collectors.toList())
        ));
  }
}

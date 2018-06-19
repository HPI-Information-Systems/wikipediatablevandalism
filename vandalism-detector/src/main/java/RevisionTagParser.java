import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;

import java.io.IOException;
import java.text.ParseException;

public class RevisionTagParser {
    private final DelimitedTextParser parser;

    public RevisionTagParser() {
        parser = new DelimitedTextParser();
        parser.setDelimiter(",");
        parser.setColumnNames(true);
        parser.setResponseIndex(new NominalAttribute("tag_id"), 2);
    }

    public AttributeDataset read(String path) throws IOException, ParseException {
        return parser.parse(path);
    }
}

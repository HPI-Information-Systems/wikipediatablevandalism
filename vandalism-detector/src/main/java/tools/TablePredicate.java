package tools;

import com.google.gson.stream.JsonReader;
import de.fau.cs.osr.ptk.common.json.AstNodeJsonTypeAdapter;
import de.fau.cs.osr.utils.TypeNameMapper;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.sweble.wikitext.engine.serialization.EngineAstNodeConverter;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import wikixmlsplit.datastructures.MyRevisionType;

/**
 * Using the JSON AST, determine if the encoded object is one of target types of tables. In order to
 * avoid parsing the entire AST, just peek the first object and read the type tag.
 *
 * @see wikixmlsplit.renderer.wikitable.WikiTable#constructNewObjects(MyRevisionType, List, boolean)
 */
public final class TablePredicate implements Predicate<String> {

  public static TablePredicate INSTANCE = new TablePredicate();

  /**
   * Attribute name to encode the contained type.
   *
   * @see AstNodeJsonTypeAdapter#SPECIAL_FIELD_TYPE
   */
  private static final String SPECIAL_FIELD_TYPE = "!type";

  private final List<String> relevantTypeNames;

  private TablePredicate() {
    relevantTypeNames = getRelevantTypeNames();
  }

  private static List<String> getRelevantTypeNames() {
    final List<String> typeNames = new ArrayList<>();
    final TypeNameMapper mapper = EngineAstNodeConverter.getTypeNameMapper();
    typeNames.add(mapper.nameForType(WtTable.class));
    typeNames.add(mapper.nameForType(WtXmlElement.class));
    return typeNames;
  }


  @Override
  public boolean test(final String ast) {
    final String typeName = parseType(ast);
    if (typeName == null) {
      throw new IllegalArgumentException("Unable to parse type name from JSON");
    }

    return relevantTypeNames.contains(typeName);
  }

  private String parseType(final String ast) {
    try (final JsonReader reader = new JsonReader(new StringReader(ast))) {
      reader.beginObject();
      final String attributeName = reader.nextName();
      if (attributeName.equals(SPECIAL_FIELD_TYPE)) {
        return reader.nextString();
      }
      return null;
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}

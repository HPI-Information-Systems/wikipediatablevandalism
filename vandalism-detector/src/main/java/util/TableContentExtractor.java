package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import lombok.val;
import lombok.var;
import model.FeatureParameters;
import org.sweble.wikitext.dumpreader.export_0_10.CommentType;
import wikixmlsplit.datastructures.MyRevisionType;

public class TableContentExtractor {

  public static String getContent(final FeatureParameters parameters) {
    return getContent(parameters.getRevision());
  }

  public static String getContent(final MyRevisionType revision) {
    var contentString = getCommentContent(revision.getComment());
    contentString += getTableContents(revision.getParsed());
    return contentString;
  }

  private static String getCommentContent(final CommentType comment) {
    if (comment == null || comment.getValue() == null) {
      return "";
    }
    return comment.getValue();
  }

  private static String getTableContents(final List<String> parsedList) {
    if (parsedList == null) {
      return "";
    }
    val tableContents = new StringBuilder();
    for (val parsed : parsedList) {
      val jsonObject = new JsonParser().parse(parsed).getAsJsonObject();
      tableContents.append(getTableContent(jsonObject));
      tableContents.append("\n");
    }
    return tableContents.toString();
  }

  private static String getTableContent(final JsonObject jsonObject) {
    val tableContent = new StringBuilder();
    if (jsonObject.has("body") && jsonObject.get("body").isJsonObject()) {
      tableContent.append(getTableContent(jsonObject.get("body").getAsJsonObject()));
    } else if (jsonObject.has("title") && jsonObject.get("title").isJsonObject()) {
      tableContent.append(getTableContent(jsonObject.get("title").getAsJsonObject()));
    } else if (jsonObject.has("target") && jsonObject.get("target").isJsonObject()) {
      tableContent.append(getTableContent(jsonObject.get("target").getAsJsonObject()));
    } else if (jsonObject.has("!list") && jsonObject.get("!list").isJsonArray()) {
      for (val element : jsonObject.get("!list").getAsJsonArray()) {
        if (element.isJsonPrimitive()) {
          tableContent.append(element.getAsString());
        } else if (element.isJsonObject()) {
          tableContent.append(getTableContent(element.getAsJsonObject()));
        }
      }
    }
    return tableContent.toString();
  }

}

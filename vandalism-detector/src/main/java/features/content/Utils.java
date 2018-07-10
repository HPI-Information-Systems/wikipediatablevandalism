package features.content;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import lombok.val;

class Utils {

  static String getTableContents(List<String> parsedList) {
    val tableContents = new StringBuilder();
    for (val parsed: parsedList) {
      val jsonObject = new JsonParser().parse(parsed).getAsJsonObject();
      tableContents.append(Utils.getTableContent(jsonObject));
      tableContents.append("\n");
    }
    return tableContents.toString();
  }

  private static String getTableContent(JsonObject jsonObject) {
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

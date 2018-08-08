package features.content.util.typing;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.val;

class DataTypeInference {

  enum CellDataType {
    Number,
    String,
    Date;
  }

  @Data(staticConstructor = "of")
  static class DataTypeInferenceResult {

    private final CellDataType type;
    private final Object value;

    double getAsDouble() {
      return (double) value;
    }

    LocalDate getAsDate() {
      return (LocalDate) value;
    }

    String getAsString() {
      return String.valueOf(value);
    }
  }


  DataTypeInferenceResult guess(final String value) {
    val dateValue = tryParseDate(value);
    if (dateValue != null) {
      return DataTypeInferenceResult.of(CellDataType.Date, dateValue);
    }

    val doubleValue = tryParseDouble(value);
    if (doubleValue != null) {
      return DataTypeInferenceResult.of(CellDataType.Number, doubleValue);
    }

    return DataTypeInferenceResult.of(CellDataType.String, value);
  }

  private static final Pattern NUMERIC_PATTERN = Pattern.compile("[^-.0-9]");

  @Nullable
  private Double tryParseDouble(final String value) {
    final String stripped = NUMERIC_PATTERN.matcher(value).replaceAll("");
    if (stripped.isEmpty() || (value.length() - stripped.length() > 10)) {
      // If the difference in size exceeds 10 characters after removing all non-digit, non-commas,
      // this likely has been a text cell.
      // Otherwise we assume to have stripped of things such as unit, currency, thousand separators.
      return null;
    }

    try {
      return Double.parseDouble(stripped);
    } catch (final NumberFormatException ignored) {
      return null;
    }
  }

  // See https://en.wikipedia.org/wiki/Wikipedia:Manual_of_Style/Dates_and_numbers#Formats
  // Of course there are many other date formats. However, for changes in the data type
  // distribution, the notion of "number of well-formatted dates according to manual"
  // is most helpful
  
  private static final Pattern pattern0 = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
  private final DateTimeFormatter format0 = new DateTimeFormatterBuilder()
      .appendValue(ChronoField.YEAR)
      .appendLiteral('-')
      .appendValue(ChronoField.MONTH_OF_YEAR)
      .appendLiteral('-')
      .appendValue(ChronoField.DAY_OF_MONTH)
      .toFormatter();

  private static final Pattern pattern1 = Pattern.compile("\\w+ \\d{4}");
  private final DateTimeFormatter format1 = new DateTimeFormatterBuilder()
      .appendText(ChronoField.MONTH_OF_YEAR)
      .appendLiteral(' ')
      .appendValue(ChronoField.YEAR)
      .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
      .toFormatter();

  private static final Pattern pattern2 = Pattern.compile("\\d{1,2} \\w+ \\d{4}");
  private final DateTimeFormatter format2 = new DateTimeFormatterBuilder()
      .appendValue(ChronoField.DAY_OF_MONTH)
      .appendLiteral(' ')
      .appendText(ChronoField.MONTH_OF_YEAR)
      .appendLiteral(' ')
      .appendValue(ChronoField.YEAR)
      .toFormatter();

  private static final Pattern pattern3 = Pattern.compile("\\w+ \\d{1,2}, \\d{4}");
  private final DateTimeFormatter format3 = new DateTimeFormatterBuilder()
      .appendText(ChronoField.MONTH_OF_YEAR)
      .appendLiteral(' ')
      .appendValue(ChronoField.DAY_OF_MONTH)
      .appendLiteral(", ")
      .appendValue(ChronoField.YEAR)
      .toFormatter();

  private final List<DateTimeFormatter> dates = asList(format0, format2, format3, format1);
  private final List<Pattern> datePatterns = asList(pattern0, pattern2, pattern3, pattern1);


  @Nullable
  private LocalDate tryParseDate(final String value) {
    for (int index = 0; index < dates.size(); ++index) {
      val pattern = datePatterns.get(index);
      val matcher = pattern.matcher(value);
      if (matcher.find()) {
        val format = dates.get(index);
        final String group = matcher.group();

        if (value.length() - group.length() > 10) {
          // We do not want to capture dates from running text
          return null;
        }

        try {
          return LocalDate.from(format.parse(group));
        } catch (final DateTimeParseException ignored) {
        }
      }
    }
    return null;
  }
}

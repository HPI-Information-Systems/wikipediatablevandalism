package features.content.util.typing;

import static org.assertj.core.api.Assertions.assertThat;

import features.content.util.typing.DataTypeInference.CellDataType;
import features.content.util.typing.DataTypeInference.DataTypeInferenceResult;
import java.time.LocalDate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Data;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DataTypeInferenceTest {

  private DataTypeInference typer;

  @BeforeEach
  void setUp() {
    typer = new DataTypeInference();
  }

  @ParameterizedTest
  @MethodSource("validNumbers")
  void testValidNumber(final Sample sample) {

    val result = typer.guess(sample.getValue());

    assertThat(result)
        .returns(CellDataType.Number, DataTypeInferenceResult::getType)
        .returns(sample.getExpected(), DataTypeInferenceResult::getAsDouble);
  }

  @ParameterizedTest
  @MethodSource("validDates")
  void testValidDate(final Sample sample) {
    val result = typer.guess(sample.getValue());

    assertThat(result)
        .returns(CellDataType.Date, DataTypeInferenceResult::getType)
        .returns(sample.getExpected(), DataTypeInferenceResult::getAsDate);
  }

  @Data(staticConstructor = "of")
  static class Sample {

    private final String value;
    private final Object expected;
  }


  private static Stream<Sample> validNumbers() {
    return Stream.concat(someInts(), someDoubles());
  }

  private static Stream<Sample> someInts() {
    return IntStream.range(-3, 3).mapToObj(i -> Sample.of(String.valueOf(i), (double) i));
  }

  private static Stream<Sample> someDoubles() {
    return Stream.of(
        Sample.of("10,000", 10_000d),
        Sample.of("10.000", 10d),
        Sample.of("0.234", 0.234d)
    );
  }

  private static Stream<Sample> validDates() {
    return Stream.of(
        Sample.of("2018-8-15", LocalDate.of(2018, 8, 15)),
        Sample.of("2018-08-15", LocalDate.of(2018, 8, 15)),
        Sample.of("July 2001", LocalDate.of(2001, 7, 1)),
        Sample.of("3 July 2001", LocalDate.of(2001, 7, 3)),
        Sample.of("July 3, 2001", LocalDate.of(2001, 7, 3))
    );
  }
}
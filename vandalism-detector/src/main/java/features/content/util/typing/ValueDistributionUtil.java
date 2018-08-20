package features.content.util.typing;

import features.content.util.typing.DataTypeInference.CellDataType;
import java.util.List;
import lombok.Data;
import wikixmlsplit.renderer.wikitable.Cell;

public class ValueDistributionUtil {

  private final DataTypeInference cellTyper = new DataTypeInference();

  @Data
  static class ValueDistribution {

    private final int[] distribution;

    int get(final CellDataType dataType) {
      return distribution[dataType.ordinal()];
    }
  }

  ValueDistribution getColumnValueDistribution(final List<Cell> cells) {
    final CellDataType[] types = CellDataType.values();
    final int[] distribution = new int[types.length];

    for (final Cell cell : cells) {
      final CellDataType type = cellTyper.guess(cell.getValue()).getType();
      distribution[type.ordinal()] += 1;
    }

    return new ValueDistribution(distribution);
  }
}

package features.content.util.typing;

import static java.util.stream.Collectors.toList;

import features.Feature;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import model.FeatureParameters;
import wikixmlsplit.renderer.wikitable.Cell;
import wikixmlsplit.renderer.wikitable.WikiTable;

@RequiredArgsConstructor
public class ValueDistributionInformationGain implements Feature {

  private final ValueDistributionUtil valueDistributionUtil;

  @Override
  public double getValue(FeatureParameters parameters) {
    val match = parameters.getRelevantMatch();
    if (match == null) {
      return -1;
    }

    final List<Cell> current = getAllCells(match.getCurrentTable());
    final List<Cell> previous = getAllCells(match.getPreviousTable());
    return computeInformationGain(current, previous);
  }

  private List<Cell> getAllCells(final WikiTable table) {
    return table.getRows().stream().flatMap(r -> r.getValues().stream()).collect(toList());
  }

  private double computeInformationGain(final List<Cell> current, final List<Cell> previous) {
    final double[] currentProbs = getProb(current);
    final double currentEntropy = entropy(currentProbs);

    final double[] previousProbs = getProb(previous);
    final double previousEntropy = entropy(previousProbs);

    return currentEntropy - previousEntropy;
  }

  private double[] getProb(final List<Cell> cells) {
    final int[] distribution = valueDistributionUtil.getColumnValueDistribution(cells)
        .getDistribution();

    double sum = 0;
    for (int i : distribution) {
      sum += i;
    }

    double[] prob = new double[distribution.length];
    for (int i = 0; i < distribution.length; ++i) {
      prob[i] = sum > 0 ? distribution[i] / sum : 0;
    }
    return prob;
  }

  private double entropy(final double[] values) {
    double sum = 0;
    for (double v : values) {
      if (v == 0) {
        continue;
      }
      sum += (-v * Math.log(v) / Math.log(2));
    }
    return sum;
  }
}

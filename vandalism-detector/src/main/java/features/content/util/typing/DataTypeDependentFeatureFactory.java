package features.content.util.typing;

import features.Feature;
import wikixmlsplit.renderer.wikitable.WikiTable;

public class DataTypeDependentFeatureFactory {

  public Feature hasNumericOutlierInColumns() {
    return new Outlier(WikiTable::getColumns, new DataTypeInference());
  }

  public Feature hasNumericOutlierInRows() {
    return new Outlier(WikiTable::getRows, new DataTypeInference());
  }

  public Feature dataTypeDistributionInformationGain() {
    return new ValueDistributionInformationGain(new ValueDistributionUtil());
  }

}

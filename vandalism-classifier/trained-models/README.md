# Vandalism Detection in Wikipedia Tables - Trained models #
This package contains trained [scikit-learn v0.19.2](http://scikit-learn.org/) python models for each individual vandalism tag. Classifiers trained only on the `baseline` feature set are located in the same-titled directory. Likewise, the directory `all-features` contains classifiers trained on the entire feature set.

## Classifier architecture ##
Each classifier is a [BalancedBaggingClassifier](http://contrib.scikit-learn.org/imbalanced-learn/stable/generated/imblearn.ensemble.BalancedBaggingClassifier.html#imblearn.ensemble.BalancedBaggingClassifier) that performes undersampling and bagging for 300 [DescisionTreeClassifiers](http://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeClassifier.html). The undersampling rate is different for each classifier.

## Persistence ##
The models were serialized using the `scikit-learn joblib` library. The following code snipped demonstrates how a model can be loaded from disk.

```python
from sklearn.externals import joblib
import os

path = os.path.join('baseline', 'tag_1.pkl')
clf = joblib.load(path)
```

## Reference ##
- Paper: Vandalism Detection in Wikipedia Tables
- Authors: Falco Duersch, Philipp Hager, Martin Schlegel
Tobias Bleifuß, Leon Bornemann, Dr. Felix Naumann
- University: Hasso-Plattner-Institut, University of Potsdam, Germany
- Date: August 2018

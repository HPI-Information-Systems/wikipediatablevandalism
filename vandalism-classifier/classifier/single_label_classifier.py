import logging

from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import cross_validate, cross_val_predict

from evaluation.utils import log_scores
from preprocessing.preprocessor import Output

logger = logging.getLogger()


class SingleLabelClassifier:

    def __init__(self, output: Output, trees: int, k_folds=3):
        self.output = output
        self.trees = trees
        self.k_folds = k_folds
        self.clf = RandomForestClassifier(n_estimators=self.trees, n_jobs=-1)
        self.test_predictions = None

    def train(self):
        logger.debug('Training single label classifier with %d trees on %d folds', self.trees, self.k_folds)
        self._cross_validate(self.output.X_train, self.output.y_train)

    def test(self):
        logger.debug('Testing single label classifier')
        self._cross_validate(self.output.X_test, self.output.y_test)
        self.test_predictions = cross_val_predict(self.clf, self.output.X_train, self.output.y_train, cv=self.k_folds)

    def _cross_validate(self, X, y):
        scores = cross_validate(self.clf, X, y,
                                scoring=['f1', 'precision', 'recall', 'roc_auc', 'accuracy'],
                                cv=self.k_folds, return_train_score=False)
        log_scores(scores)

    def _test_predictions(self):
        return self.test_predictions

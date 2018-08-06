import logging

from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import cross_validate

from evaluation.utils import log_scores
from preprocessing.preprocessor import Output

logger = logging.getLogger()


class SingleLabelClassifier:

    def __init__(self, output: Output, trees: int, k_folds=3):
        self.output = output
        self.trees = trees
        self.k_folds = k_folds
        self.clf = RandomForestClassifier(n_estimators=self.trees, n_jobs=-1)

    def train(self):
        logger.debug('Training single label classifier with %d trees on %d folds', self.trees, self.k_folds)
        scores = cross_validate(self.clf, self.output.X_test, self.output.y_test,
                                scoring=['f1', 'precision', 'recall', 'roc_auc', 'accuracy'],
                                cv=self.k_folds, return_train_score=False)
        log_scores(scores)
        return scores

    def test(self):
        logger.debug('Testing single label classifier')
        self.clf.predict_proba(self.output.X_train)

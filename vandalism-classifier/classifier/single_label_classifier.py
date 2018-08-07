import logging

from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import cross_validate, cross_val_predict

from evaluation.utils import log_scores
from preprocessing.preprocessor import Output

logger = logging.getLogger()


class SingleLabelClassifier:

    def __init__(self, output: Output, trees: int, k_folds=10):
        self.output = output
        self.trees = trees
        self.k_folds = k_folds
        self.clf = RandomForestClassifier(n_estimators=self.trees, n_jobs=-1)

    def train_cross_validate(self):
        logger.debug('Training single label classifier with %d trees on %d folds', self.trees, self.k_folds)
        scores = cross_validate(self.clf, self.output.X_train, self.output.y_train,
                                scoring=['f1', 'precision', 'recall', 'roc_auc', 'accuracy'],
                                cv=self.k_folds, n_jobs=-1, return_train_score=False)
        log_scores(scores)
        return scores

    def train_predict(self):
        logger.debug('Retrieving probabilities with %d trees on %d folds', self.trees, self.k_folds)

        classes = cross_val_predict(self.clf, self.output.X_train, self.output.y_train,
                                    cv=self.k_folds, n_jobs=-1, method='predict')
        probabilities = cross_val_predict(self.clf, self.output.X_train, self.output.y_train,
                                          cv=self.k_folds, n_jobs=-1, method='predict_proba')
        return classes, probabilities

    def test_predict(self):
        logger.debug('Retrieving probabilities with %d trees on %d folds', self.trees, self.k_folds)
        self.clf.fit(self.output.X_train, self.output.y_train)

        classes = self.clf.predict(self.output.X_test)
        probabilities = self.clf.predict_proba(self.output.X_test)
        return classes, probabilities

    def feature_importance(self):
        return self.clf.feature_importances_

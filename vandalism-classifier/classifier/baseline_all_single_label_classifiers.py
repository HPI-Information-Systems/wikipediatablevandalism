import logging

import os

import pandas as pd

from classifier.single_label_classifier import SingleLabelClassifier
from evaluation.utils import create_classification_report, create_receiver_operation_characteristic_graph, \
    create_precision_recall_graph, create_feature_importance_graph, false_negatives, create_revisions_by_tag_graph
from preprocessing.features import BASELINE_FEATURE_COLUMNS
from preprocessing.preprocessor import Preprocessor
from preprocessing.tags import Tags

logger = logging.getLogger()


class BaselineSingleLabelClassifiers:

    def __init__(self, features, tags, report_output, trees: int, k_folds=10):
        self.features = features
        self.tags = pd.read_csv(tags)
        self.report_output = report_output
        self.trees = trees
        self.k_folds = k_folds

    def run_all(self):
        tags = [
            Tags.BLANKING,
            Tags.NONSENSE,
            Tags.QUALITY_ISSUE,
            Tags.FALSE_FACT,
            Tags.SYNTAX,
            Tags.EDIT_WARS,
            Tags.INTENTION
        ]

        for tag in tags:
            tag_name = tag.name.lower()
            logger.debug('Processing %s', tag)
            preprocessor = Preprocessor(self.features, [tag], BASELINE_FEATURE_COLUMNS, vandalism_sample_rate=0.3)
            data = preprocessor.run()
            clf = SingleLabelClassifier(data, self.trees)

            # Training (Undersampled)
            y_train_predict, y_train_predict_proba = clf.train_predict()
            y_train = data.y_train
            logger.debug(create_classification_report(y_train, y_train_predict))

            plt, pr_auc = create_precision_recall_graph(y_train, y_train_predict_proba[:, 1])
            plt.savefig(os.path.join(self.report_output, 'baseline_' + tag_name + '_train_pr_auc.png'))
            plt.show()
            print("PR-AUC", pr_auc)

            plt, roc_auc = create_receiver_operation_characteristic_graph(y_train, y_train_predict_proba[:, 1])
            plt.savefig(os.path.join(self.report_output, 'baseline_' + tag_name + '_train_roc_auc.png'))
            plt.show()
            print("ROC-AUC", roc_auc)

            # Testing
            y_predict, y_predict_proba = clf.test_predict()
            y = data.y_test
            logger.debug(create_classification_report(y, y_predict))

            plt, pr_auc = create_precision_recall_graph(y, y_predict_proba[:, 1])
            plt.savefig(os.path.join(self.report_output, 'baseline_' + tag_name + '_test_pr_auc.png'))
            plt.show()
            print("PR-AUC", pr_auc)

            plt, roc_auc = create_receiver_operation_characteristic_graph(y, y_predict_proba[:, 1])
            plt.savefig(os.path.join(self.report_output, 'baseline_' + tag_name + '_test_roc_auc.png'))
            plt.show()
            print("ROC-AUC", roc_auc)

            # False negatives
            fn = false_negatives(data.X_test_with_meta, y, y_predict)
            plt = create_revisions_by_tag_graph(fn, self.tags)
            plt.savefig(os.path.join(self.report_output, 'baseline_' + tag_name + '_false_negatives.png'))
            plt.show()

            # Feature importance
            plt = create_feature_importance_graph(clf, data.labels)
            plt.savefig(os.path.join(self.report_output, 'baseline_' + tag_name + '_test_feature_importance.png'))
            plt.show()

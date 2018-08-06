import logging
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from ipywidgets import HTML
from sklearn.metrics import classification_report, confusion_matrix, precision_recall_curve, average_precision_score, \
    auc, roc_curve
from sklearn.model_selection import cross_val_predict

logger = logging.getLogger()


def log_scores(scores):
    logger.debug('Scores')
    for name, values in scores.items():
        score = ": %0.2f (+/- %0.2f)" % (values.mean(), values.std() * 2)
        logger.debug("%s : %s", name, score)


def create_classification_report(y, y_predict):
    classes = ['No Vandalism', 'Vandalism']
    return classification_report(y, y_predict, target_names=classes)


def create_confusion_matrix(y, y_predict):
    columns = ['Predicted No Vandalism', 'Predicted Vandalism']
    index = ['True No Vandalism', 'True Vandalism']
    cf_matrix = confusion_matrix(y, y_predict)
    return pd.DataFrame(cf_matrix, columns=columns, index=index)


def create_precision_recall_graph(y, y_predict):
    precision, recall, thresholds = precision_recall_curve(y, y_predict)
    average_precision = average_precision_score(y, y_predict)
    pr_auc = auc(recall, precision)

    plt.title('Precision-Recall: Average Precision=  %0.2f' % average_precision)
    plt.step(recall, precision, color='b', where='post')
    plt.xticks(np.arange(0, 1.1, 0.1))
    plt.yticks(np.arange(0, 1.1, 0.1))
    plt.ylabel('Precision')
    plt.xlabel('Recall')
    return plt, pr_auc


def create_receiver_operation_characteristic_graph(y, y_predict):
    false_positive_rate, true_positive_rate, thresholds = roc_curve(y, y_predict)
    roc_auc = auc(false_positive_rate, true_positive_rate)

    plt.title('Receiver Operating Characteristic')
    plt.plot(false_positive_rate, true_positive_rate, 'b', label='AUC = %0.2f' % roc_auc)
    plt.legend(loc='lower right')
    plt.plot([0, 1], [0, 1], 'r--')
    plt.xlim([0, 1])
    plt.ylim([0, 1])
    plt.ylabel('True Positive Rate')
    plt.xlabel('False Positive Rate')
    return plt, roc_auc


def create_error_matrix_graph(y, y_predict):
    cf_matrix = confusion_matrix(y, y_predict)
    plt.matshow(cf_matrix, cmap=plt.cm.gray)
    return plt


def create_feature_importance_graph(clf, labels):
    feature_importance = clf.feature_importance()
    x_pos = np.arange(len(feature_importance))

    plt.gcf().set_size_inches(14, 6)
    plt.bar(x_pos, feature_importance, align='center')
    plt.xticks(x_pos, labels, rotation='vertical')
    plt.ylabel('Feature Importance')
    return plt


def false_negatives(X, y, y_predict):
    return X[(y == 1) & (y_predict == 0)]


def false_positives(X, y, y_predict):
    return X[(y == 0) & (y_predict == 1)]


def create_revision_link_html(revisions, tags, n=10):
    revisions_with_labels = revisions.merge(tags, how='left', left_on='tag_id', right_on='id')
    html = []
    for index, item in revisions_with_labels.head(n).iterrows():
        item_html = '<li>{tag}: <a href="http://en.wikipedia.org/index.php?diff={rev_id}">{rev_id} </a></li>' \
            .format(rev_id=int(item['revision_id']), tag=item['name'])
        html.append(item_html)
    return HTML('<ul>' + ''.join(html) + '</ul>')


def create_revisions_by_tag_graph(revisions, tags):
    revisions_with_labels = revisions.merge(tags, how='left', left_on='tag_id', right_on='id')
    tag_counts = revisions_with_labels.name.value_counts()
    tag_indices = np.arange(len(tag_counts))
    plt.bar(tag_indices, tag_counts)
    plt.xticks(tag_indices, tag_counts.index, rotation=45)
    return plt

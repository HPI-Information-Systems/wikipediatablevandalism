import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
from sklearn.metrics import classification_report, confusion_matrix, precision_recall_curve, average_precision_score, auc, roc_curve
from sklearn.feature_selection import mutual_info_classif
from scipy import interp

def plot_confusion_matrix(y_true, y_pred):
    vandalism_count = y_true.value_counts()[True]
    no_vandalism_count = y_true.value_counts()[False]
    cfn_matrix = confusion_matrix(y_true, y_pred)
    cfn_norm_matrix = np.array([[1.0/no_vandalism_count, 1.0/no_vandalism_count], [1.0/vandalism_count, 1.0/vandalism_count]])
    norm_cfn_matrix = cfn_matrix * cfn_norm_matrix
    
    fig = plt.figure(figsize=(15,5))
    ax = fig.add_subplot(1,2,1)
    sns.heatmap(cfn_matrix, annot=True, fmt="d", linewidths=0.5, ax=ax)
    plt.title('Confusion Matrix')
    plt.ylabel('Real Classes')
    plt.xlabel('Predicted Classes')

    ax = fig.add_subplot(1,2,2)
    sns.heatmap(norm_cfn_matrix, linewidths=0.5, annot=True, ax=ax)
    plt.title('Normalized Confusion Matrix')
    plt.ylabel('Real Classes')
    plt.xlabel('Predicted Classes')
    plt.show()
    
    print('Classification Report')
    classes = ['No Vandalism', 'Vandalism']
    print(classification_report(y_true, y_pred, target_names=classes))
    
def plot_scores(scores):
    print('Cross validation scores')
    scores = list(scores.items())[2:]

    for score in scores:
        sns.lineplot(range(0, 10), score[1], label=score[0])
        plt.legend(bbox_to_anchor=(1.1, 1.05), frameon=False)
    plt.show()

def plot_precision_recall(y_true, y_predict_proba):
    precision, recall, thresholds = precision_recall_curve(y_true, y_predict_proba)
    average_precision = average_precision_score(y_true, y_predict_proba)
    pr_auc = auc(recall, precision)

    plt.title('Precision-Recall')
    plt.step(recall, precision, color='b', where='post', label='AUC = %0.2f' % pr_auc)
    plt.xticks(np.arange(0, 1.1, 0.1))
    plt.yticks(np.arange(0, 1.1, 0.1))
    plt.ylabel('Precision')
    plt.xlabel('Recall')
    plt.legend(loc='lower right', frameon=False)
    plt.show()
    
def plot_roc(Y_true, Y_predict_proba, output_path):
    fpr, tpr, _ = roc_curve(Y_true, Y_predict_proba)
    roc_auc = auc(fpr, tpr)
    
    plt.figure(figsize=(4, 4))
    plt.plot(fpr, tpr,label='vandalism (AUC = %0.2f)' % roc_auc)
    plt.plot([0, 1], [0, 1], 'k--')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.legend(loc=9, bbox_to_anchor=(0.5, -0.15), ncol=2, frameon=False)
    plt.savefig(output_path, bbox_inches='tight')
    plt.show()


def plot_feature_importance(clf, feature_labels):
    # Feature importance (weights in random forrest)
    feature_importance = clf.feature_importances_
    x_pos = np.arange(len(feature_importance))

    plt.gcf().set_size_inches(14, 6)
    plt.bar(x_pos, feature_importance, align='center')
    plt.xticks(x_pos, feature_labels, rotation='vertical')
    plt.ylabel('Feature Importance')
    plt.show()
    return feature_importance


def plot_information_gain(X, y):
    feature_labels = X.columns
    information_gain = mutual_info_classif(X, y)
    x_pos = np.arange(len(information_gain))

    plt.gcf().set_size_inches(14, 6)
    plt.bar(x_pos, information_gain, align='center')
    plt.xticks(x_pos, feature_labels, rotation='vertical')
    plt.ylabel('Information Gain')
    plt.show()
    return information_gain


def plot_multilabel_classification_report(Y_true, Y_predict, tag_names):
    print(classification_report(Y_true, Y_predict, target_names=tag_names))


def plot_multilabel_precision_recall(Y_true, Y_predict_proba, tags, tag_names, output_path=''):
    precision = dict()
    recall = dict()
    average_precision = dict()

    for i in range(0, len(Y_true[0])):
        precision[i], recall[i], _ = precision_recall_curve(Y_true[:, i], Y_predict_proba[:, i])
        average_precision[i] = average_precision_score(Y_true[:, i], Y_predict_proba[:, i])

    plt.figure(figsize=(6, 6))
    f_scores = np.linspace(0.2, 0.8, num=4)
    lines = []
    labels = []
    for f_score in f_scores:
        x = np.linspace(0.01, 1)
        y = f_score * x / (2 * x - f_score)
        l, = plt.plot(x[y >= 0], y[y >= 0], color='gray', alpha=0.2)
        plt.annotate('f1={0:0.1f}'.format(f_score), xy=(0.9, y[45] + 0.02))

    lines.append(l)
    labels.append('iso-f1 curves')
    colors = plt.get_cmap('tab20').colors

    for i in range(len(tag_names)):
        l, = plt.plot(recall[i], precision[i], lw=2, color = colors[tags[i] - 1])
        lines.append(l)
        labels.append('{0} (AUC = {1:0.2f})'
                    ''.format(tag_names[i], average_precision[i]))

    fig = plt.gcf()
    fig.subplots_adjust(bottom=0.25)
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('Recall')
    plt.ylabel('Precision')

    plt.legend(lines, labels, loc=9, bbox_to_anchor=(0.5, -0.15), ncol=2, frameon=False)
    plt.savefig(output_path, bbox_inches='tight')
    plt.show()


def plot_multilabel_roc(Y_true, Y_predict_proba, tags, tag_names, output_path):
    n_classes = Y_true.shape[1]
    # Compute ROC curve and ROC area for each class
    fpr = dict()
    tpr = dict()
    roc_auc = dict()
    for i in range(n_classes):
        fpr[i], tpr[i], _ = roc_curve(Y_true[:, i], Y_predict_proba[:, i])
        roc_auc[i] = auc(fpr[i], tpr[i])

    # Compute micro-average ROC curve and ROC area
    fpr["micro"], tpr["micro"], _ = roc_curve(Y_true.ravel(), Y_predict_proba.ravel())
    roc_auc["micro"] = auc(fpr["micro"], tpr["micro"])

    # Compute macro-average ROC curve and ROC area
    # First aggregate all false positive rates
    all_fpr = np.unique(np.concatenate([fpr[i] for i in range(n_classes)]))

    # Then interpolate all ROC curves at this points
    mean_tpr = np.zeros_like(all_fpr)
    for i in range(n_classes):
        mean_tpr += interp(all_fpr, fpr[i], tpr[i])

    # Finally average it and compute AUC
    mean_tpr /= n_classes

    fpr["macro"] = all_fpr
    tpr["macro"] = mean_tpr
    roc_auc["macro"] = auc(fpr["macro"], tpr["macro"])
    colors = plt.get_cmap('tab20').colors

    # Plot all ROC curves
    plt.figure(figsize=(6, 6))
    plt.plot(fpr["micro"], tpr["micro"],
            label='Micro-average ROC curve (area = {0:0.2f})'
                ''.format(roc_auc["micro"]),
            color='deeppink', linestyle=':', linewidth=4)

    plt.plot(fpr["macro"], tpr["macro"],
            label='Macro-average ROC curve (area = {0:0.2f})'
                ''.format(roc_auc["macro"]),
            color='navy', linestyle=':', linewidth=4)

    for i in range(n_classes):
        plt.plot(fpr[i], tpr[i],
                label='{0} (AUC = {1:0.2f})'
                ''.format(tag_names[i], roc_auc[i]),
                color=colors[tags[i] - 1])

    plt.plot([0, 1], [0, 1], 'k--')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.legend(loc=9, bbox_to_anchor=(0.5, -0.15), ncol=2, frameon=False)
    plt.savefig(output_path, bbox_inches='tight')
    plt.show()

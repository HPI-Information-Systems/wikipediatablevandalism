import json
import subprocess
import os
from sklearn.externals import joblib

MODEL_DIR = 'models'

def get_git_revision_hash():
    return subprocess.check_output(['git', 'rev-parse', 'HEAD'])\
        .decode("utf-8")\
        .replace('\n', '')\
        .replace('\r', '')

def save_model(tag_id, clf, train_scores, grid_search):
    if not os.path.exists(MODEL_DIR):
        os.makedirs(MODEL_DIR)
    
    # Pickle dump classifier to disk
    model_path = os.path.join(MODEL_DIR, 'tag_%d.pkl' % tag_id)
    joblib.dump(clf, model_path)

    # Dump meta data
    params = clf.get_params()
    # Convert custom estimator function refs to string
    ratio = params['ratio'] if type(params['ratio']) is str else str(params['ratio'].__name__)
    for i in range(len(grid_search.cv_results_['params'])):
        r = grid_search.cv_results_['params'][i]['ratio']
        grid_search.cv_results_['params'][i]['ratio'] = r if type(r) is str else str(r.__name__)

    meta = {
        'tag_id': tag_id,
        'git_hash': get_git_revision_hash(),
        'n_estimators': params['n_estimators'],
        'max_features': params['max_features'],
        'ratio': ratio,
        'train_scores': {k: v.tolist() for k, v in train_scores.items()},
        'grid_search': {
            'params': grid_search.cv_results_['params'],
            'mean_test_recall': grid_search.cv_results_['mean_test_recall'].tolist(),
            'mean_test_precision': grid_search.cv_results_['mean_test_precision'].tolist(),
            'mean_test_f1': grid_search.cv_results_['mean_test_f1'].tolist(),
            'mean_test_f1': grid_search.cv_results_['mean_test_f1_vandalism'].tolist(),
            'mean_test_roc_auc': grid_search.cv_results_['mean_test_roc_auc'].tolist()
        }
    }
    
    meta_path = os.path.join(MODEL_DIR, 'tag_%d.meta' % tag_id)
    with open(meta_path, 'w') as f:
        json.dump(meta, f)


def load_meta(file_name):
    path = os.path.join(MODEL_DIR, file_name + '.meta')
    with open(path, 'r') as f:
        return json.load(f)

def load_clf(file_name):
    path = os.path.join(MODEL_DIR, file_name + '.pkl')
    return joblib.load(path)

def clfs_are_same_version(clfs):
    hashes = [clf['meta']['git_hash'] for tag_id, clf in clfs.items()]
    return len(set(hashes)) <= 1

def load_all_classifiers():
    classifiers = {}
    
    files = []
    for f in os.listdir(MODEL_DIR):
        if f.endswith(".pkl"):
            files.append(f)

    for file in sorted(files):
        filename = os.path.splitext(file)[0]
        print('Loading model', filename)
        meta = load_meta(filename)
        clf = load_clf(filename)
        tag_id = meta['tag_id']
        classifiers[tag_id] = {
            'clf': clf,
            'meta': meta
        }
        
    assert clfs_are_same_version(classifiers), 'Warning loaded classifiers were not generated on the same git hash!'
    return classifiers

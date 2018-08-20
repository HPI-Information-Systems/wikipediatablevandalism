from sklearn.base import BaseEstimator, TransformerMixin
import numpy as np

class VandalismEncoder(BaseEstimator, TransformerMixin):
    def __init__(self, tag_ids):
        self.tag_ids = tag_ids
        
    def fit(self, X, y=None):
        return self

    def transform(self, X):
        X['is_vandalism'] = X.tag_id.isin(self.tag_ids)
        X = X.drop(['tag_id'], axis=1)
        return X

    def fit_transform(self, X, y):
        return self.transform(X)
    
class RevisionGrouper(BaseEstimator, TransformerMixin):        
    def fit(self, X, y=None):
        return self

    def transform(self, X):
        X = X.groupby(X.index).apply(self._vandalism_revision_or_first)
        return X

    def fit_transform(self, X, y=None):
        return self.transform(X)
    
    def _vandalism_revision_or_first(self, group):
        vandalism_revisions = group.loc[group['is_vandalism'] == True]
        return vandalism_revisions.iloc[0] if len(vandalism_revisions) > 0 else group.iloc[0]

class FeatureSelector(BaseEstimator, TransformerMixin):        
    def __init__(self, columns=None):
        self.columns = columns
        
    def fit(self, X, y=None):
        return self

    def transform(self, X):
        if self.columns != None:
            if 'is_vandalism' not in self.columns:
                # Do not remove 'is_vandalism'
                self.columns.append('is_vandalism')
            X = X[self.columns]
        return X
    
    def fit_transform(self, X, y=None):
        return self.transform(X)


class MultilabelFeatureSelector(BaseEstimator, TransformerMixin):        
    def __init__(self, columns=None):
        self.columns = columns
        
    def fit(self, X, y=None):
        return self

    def transform(self, X):
        if self.columns != None:
            if 'tag_id' not in self.columns:
                # Do not remove 'is_vandalism'
                self.columns.append('tag_id')
            X = X[self.columns]
        return X
    
    def fit_transform(self, X, y=None):
        return self.transform(X)


class TagGrouper(BaseEstimator, TransformerMixin):
    def fit(self, X, y=None):
        return self

    def transform(self, X):
        X['all_tags'] = X.groupby(['revision_id'])['tag_id'].apply(list)
        X = X.drop(['tag_id'], axis=1)
        X = X.groupby(['revision_id']).first()
        return X

    def fit_transform(self, X, y):
        return self.transform(X)


def flatten_multilabel_predict_proba(y_score):
    result = []
    for i in range(len(y_score[0])):
        row = []
        for l in range(len(y_score)):
            row.append(y_score[l][i][1]) 
        result.append(row)
    return np.array(result)

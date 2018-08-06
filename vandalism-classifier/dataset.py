import pandas as pd


def read_dataframe(path: str):
    return pd.read_csv(path)


def get_xy(df: pd.DataFrame):
    y = df['is_vandalism']
    X = df.drop(['is_vandalism'], axis=1)
    return X, y


def get_labels(X: pd.DataFrame):
    return X.columns


def get_revisions(X: pd.DataFrame):
    revisions = X['revision_id']
    X.drop(['revision_id'], axis=1, inplace=True)
    return revisions


def get_tags(X: pd.DataFrame):
    tags = X['tag_id']
    X.drop(['tag_id'], axis=1, inplace=True)
    return tags

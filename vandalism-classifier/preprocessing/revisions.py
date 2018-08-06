import pandas as pd


def count_revisions(df: pd.DataFrame):
    return df['revision_id'].nunique()


def group_vandalism_by_revision(df: pd.DataFrame):
    return df.groupby(['revision_id']).apply(vandalism_revision_or_first)


def vandalism_revision_or_first(df: pd.DataFrame):
    vandalism_revisions = df.loc[df['is_vandalism'] == 1]
    return vandalism_revisions.iloc[0] if len(vandalism_revisions) > 0 else df.iloc[0]

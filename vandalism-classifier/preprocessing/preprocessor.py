import logging

from sklearn.model_selection import train_test_split

from dataset import read_dataframe, get_xy, get_labels
from preprocessing.features import select_features
from preprocessing.revisions import count_revisions, group_vandalism_by_revision
from preprocessing.tags import mark_tags_as_vandalism, Tags
from sampler.sampler import UnderSampler

logger = logging.getLogger()


class Preprocessor:

    def __init__(self, path: str, tags: [Tags], feature_columns, vandalism_sample_rate=0.3):
        self.vandalism_sample_rate = vandalism_sample_rate
        self.tags = tags
        self.feature_columns = feature_columns
        self.path = path

    def run(self):
        df = read_dataframe(self.path)
        # Select/Discard features
        if self.feature_columns:
            df = select_features(df, self.feature_columns)
        # Mark selected tags as vandalism
        df = mark_tags_as_vandalism(df, self.tags)
        # Group all rows by revision id
        logger.debug('Number of rows in df before grouping: %d. Unique revisions: %d', len(df), count_revisions(df))
        df = group_vandalism_by_revision(df)
        logger.debug('Number of rows in df after grouping: %d. Unique revisions: %d', len(df), count_revisions(df))
        # Split data frame into individual datasets
        X, y = get_xy(df)
        # Split into train and test sets
        X_train, X_test, y_train, y_test = train_test_split(X, y)
        # Strip revision ids
        train_revisions = X_train['revision_id']
        test_revisions = X_test['revision_id']
        X_test_with_meta = X_test
        X_train = X_train.drop(['revision_id'], axis=1)
        X_test = X_test.drop(['revision_id'], axis=1)
        X_train.reset_index()
        X_test.reset_index()
        # Strip tags
        train_tags = X_train['tag_id']
        test_tags = X_test['tag_id']
        X_train = X_train.drop(['tag_id'], axis=1)
        X_test = X_test.drop(['tag_id'], axis=1)
        # Strip feature labels
        labels = get_labels(X_train)
        # Undersample trainings set
        sampler = UnderSampler(X_train, y_train)
        X_train_sampled, y_train_sampled = sampler.resample(self.vandalism_sample_rate)

        return Output(X_train_sampled, y_train_sampled,
                      X_test, y_test,
                      X_test_with_meta,
                      train_revisions, test_revisions,
                      train_tags, test_tags,
                      labels)


class Output:

    def __init__(self,
                 X_train,
                 y_train,
                 X_test,
                 y_test,
                 X_test_with_meta,
                 train_revisions,
                 test_revisions,
                 train_tags,
                 test_tags,
                 labels):
        self.X_train = X_train
        self.y_train = y_train
        self.X_test = X_test
        self.y_test = y_test
        self.X_test_with_meta = X_test_with_meta
        self.train_revisions = train_revisions
        self.test_revisions = test_revisions
        self.train_tags = train_tags
        self.test_tags = test_tags
        self.labels = labels

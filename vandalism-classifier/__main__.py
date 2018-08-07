import logging

import click

from classifier.all_single_label_classifiers import SingleLabelClassifierRunner
from classifier.baseline_all_single_label_classifiers import BaselineSingleLabelClassifiers
from classifier.single_label_classifier import SingleLabelClassifier
from preprocessing.features import BASELINE_FEATURE_COLUMNS
from preprocessing.preprocessor import Preprocessor
from preprocessing.tags import Tags

logging.basicConfig(format='%(levelname)s: %(message)s', level=logging.DEBUG)
logging.basicConfig(filename='logs/output.log', level=logging.DEBUG)


@click.group()
def cli():
    pass


@cli.command()
@click.option('--features', required=True, help='Path to feature.csv')
def single_label(features):
    """Classifies revisions into vandalism or non-vandalism"""
    preprocessor = Preprocessor(features, [Tags.BLANKING], BASELINE_FEATURE_COLUMNS)
    output = preprocessor.run()
    clf = SingleLabelClassifier(output, 300)
    clf.train_cross_validate()


@cli.command()
@click.option('--features', required=True, help='Path to feature.csv')
@click.option('--tags', required=True, help='Path to tag.csv')
@click.option('--baseline_only', is_flag=True, default=False, help='Path to tag.csv')
def all_single_labels(features, tags, baseline_only):
    """Classifies revisions into vandalism or non-vandalism"""
    if baseline_only:
        runner = BaselineSingleLabelClassifiers(features, tags, './output/', 300)
        runner.run_all()
    else:
        runner = SingleLabelClassifierRunner(features, tags, './output/', 300)
        runner.run_all()


if __name__ == '__main__':
    cli()

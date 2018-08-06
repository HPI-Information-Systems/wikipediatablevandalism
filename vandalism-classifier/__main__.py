import logging

import click

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
    clf.train()
    clf.test()


if __name__ == '__main__':
    cli()

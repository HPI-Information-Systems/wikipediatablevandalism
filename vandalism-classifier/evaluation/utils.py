import logging

logger = logging.getLogger()


def log_scores(scores):
    logger.debug('Scores')
    for name, values in scores.items():
        score = ": %0.2f (+/- %0.2f)" % (values.mean(), values.std() * 2)
        logger.debug("%s : %s", name, score)

import logging

import pandas as pd
from imblearn.under_sampling import RandomUnderSampler

logger = logging.getLogger()


class UnderSampler:
    def __init__(self, x: pd.DataFrame, y: pd.Series):
        self.x = x
        self.y = y

    def resample(self, vandalism_rate):
        logger.debug('Resamping vandalism rate: %s, wanted: %s', self.get_vandalism_rate(), vandalism_rate)
        logger.debug('Dataset before resampling: %d total. Vandalism: %s. No Vandalism: %s',
                     self.get_total_count(), self.get_vandalism_count(), self.get_non_vandalism_count())

        vandalism, no_vandalism, total = self.get_absolute_sample_counts(vandalism_rate)
        sampler = RandomUnderSampler(ratio={0: no_vandalism, 1: vandalism})

        logger.debug('Dataset after resampling: %d total. Vandalism: %s. No Vandalism: %s',
                     total, vandalism, no_vandalism)

        x_sampled, y_sampled = sampler.fit_sample(self.x, self.y)
        return pd.DataFrame(x_sampled, columns=self.x.columns), pd.Series(y_sampled)

    def get_absolute_sample_counts(self, vandalism_rate):
        vandalism_count = self.get_vandalism_count()
        non_vandalism_count = self.get_non_vandalism_count()
        target_vandalism_count = vandalism_count
        target_non_vandalism_count = (vandalism_count // vandalism_rate) - vandalism_count

        if target_non_vandalism_count > non_vandalism_count:
            logger.warning('Too little non-vandalism in dataset. Wanted %d samples, set to max %d',
                           target_non_vandalism_count, non_vandalism_count)
            target_non_vandalism_count = non_vandalism_count

        total = target_vandalism_count + target_non_vandalism_count
        return int(target_vandalism_count), int(target_non_vandalism_count), total

    def get_vandalism_count(self):
        return self.y.value_counts()[1]

    def get_non_vandalism_count(self):
        return self.y.value_counts()[0]

    def get_total_count(self):
        return len(self.y)

    def get_vandalism_rate(self):
        return self.get_vandalism_count() / self.get_total_count()

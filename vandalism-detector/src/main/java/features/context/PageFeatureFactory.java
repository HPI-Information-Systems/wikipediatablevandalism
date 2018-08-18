package features.context;

import com.google.common.collect.Lists;
import features.Feature;
import features.context.impl.ArticleTemperature;
import features.context.impl.AuthorDiversity;
import features.context.impl.AuthorRank;
import features.context.impl.EditActivity;
import features.context.impl.RevisionProvider;
import features.context.util.ContributorRevertedBeforeInThatArticle;
import features.context.util.TimeSinceFirstRevisionBySameContributor;
import java.time.Duration;
import java.time.Period;
import lombok.val;
import util.BasicUtils;

/**
 * Create all features based on the metadata connected to the whole page (future and past)
 */
class PageFeatureFactory {

  Feature timeSinceLastArticleEdit() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      if (previousRevision == null) {
        return -1;
      }
      return BasicUtils.getTimeDuration(parameters.getRevision(), previousRevision);
    };
  }

  Feature hasPreviousSameContributor() {
    return parameters -> {
      val previousRevision = parameters.getPreviousRevision();
      if (previousRevision == null) {
        return 0;
      }
      return BasicUtils.hasSameContributor(parameters.getRevision(), previousRevision)
          ? 1 : 0;
    };
  }

  Feature timeSinceLastArticleEditBySameContributor() {
    return parameters -> TimeSinceFirstRevisionBySameContributor
        .getTime(parameters.getRevision(), parameters.getPreviousRevisions());
  }

  Feature timeSinceFirstArticleEditBySameContributor() {
    return parameters -> TimeSinceFirstRevisionBySameContributor
        .getTime(parameters.getRevision(), Lists.reverse(parameters.getPreviousRevisions()));
  }

  Feature revertCount() { // wie oft gab es die aktuelle revision schonmal, > 1 means is revert
    return parameters -> {
      int revertCount = 0;
      for (val previousRevision : parameters.getPreviousRevisions()) {
        if (parameters.getRevision().getSha1().equals(previousRevision.getSha1())) {
          ++revertCount;
        }
      }
      return revertCount;
    };
  }

  Feature prevRevertCount() { // wie oft gab es die vorherige revision schonmal, > 1 means is revert
    return parameters -> {
      val previousRevisions = parameters.getPreviousRevisions();
      if (previousRevisions.size() == 0) {
        return 0;
      }

      int prevRevertCount = 0;
      val previousRevision0 = previousRevisions.get(0);
      previousRevisions.remove(0);
      for (val previousRevision : previousRevisions) {
        if (previousRevision0.getSha1().equals(previousRevision.getSha1())) {
          ++prevRevertCount;
        }
      }
      return prevRevertCount;
    };
  }

  Feature ratioOffAllEditsToContributorEdits() {
    return parameters -> {
      val previousRevisions = parameters.getPreviousRevisions();
      if (previousRevisions == null) {
        return -1;
      }
      double sameContributor = 0;
      for (val previousRevision : previousRevisions) {
        if (BasicUtils.hasSameContributor(parameters.getRevision(), previousRevision)) {
          ++sameContributor;
        }
      }

      val previousRevisionCount = parameters.getPreviousRevisions().size();
      return previousRevisionCount > 0 ? sameContributor / previousRevisionCount : 0;
    };
  }

  Feature contributorRevertedBeforeInThatArticleRatio() {
    return ContributorRevertedBeforeInThatArticle::getRevertedRatio;
  }

  Feature timeSinceContributorRevertedBeforeInThatArticle() {
    return ContributorRevertedBeforeInThatArticle::getTimeSinceLastReverted;
  }

  Feature articleTemperatureAll() {
    return ArticleTemperature::getAll;
  }

  Feature articleTemperatureYear() {
    return ArticleTemperature::getYear;
  }

  Feature articleTemperatureMonth() {
    return ArticleTemperature::getMonth;
  }

  Feature articleTemperatureWeek() {
    return ArticleTemperature::getWeek;
  }

  Feature articleTemperatureDay() {
    return ArticleTemperature::getDay;
  }

  Feature articleTemperatureHour() {
    return ArticleTemperature::getHour;
  }

  Feature articleTemperatureRatio() {
    return ArticleTemperature::getRatioHourToMonth;
  }

  Feature authorRank() {
    return new AuthorRank(RevisionProvider.all());
  }

  Feature authorRankOfLast200Edits() {
    return new AuthorRank(RevisionProvider.lastN(200));
  }

  Feature authorRankOfLastMonth() {
    return new AuthorRank(RevisionProvider.maxAge(Period.ofDays(30)));
  }

  Feature authorRankOfLast200EditsOfOneMonth() {
    return new AuthorRank(RevisionProvider.lastNWithMaxAge(200, Period.ofDays(30)));
  }

  Feature distinctAuthorCountOfLast5Edits() {
    return new AuthorDiversity(RevisionProvider.lastN(5));
  }

  Feature distinctAuthorCountOfLast20Edits() {
    return new AuthorDiversity(RevisionProvider.lastN(20));
  }

  Feature distinctAuthorCountOfLast80Edits() {
    return new AuthorDiversity(RevisionProvider.lastN(80));
  }

  Feature distinctAuthorCountOfLastHour() {
    return new AuthorDiversity(RevisionProvider.maxAge(Duration.ofHours(1)));
  }

  Feature distinctAuthorCountOfLastTwoHours() {
    return new AuthorDiversity(RevisionProvider.maxAge(Duration.ofHours(2)));
  }

  Feature distinctAuthorCountOfLast24Hours() {
    return new AuthorDiversity(RevisionProvider.maxAge(Duration.ofDays(1)));
  }

  Feature editActivityIncreaseOfTwoHours() {
    return EditActivity.increaseComparingDurationOf(Duration.ofHours(2));
  }

  Feature editActivityIncreaseOfOneDay() {
    return EditActivity.increaseComparingDurationOf(Duration.ofDays(1));
  }

  Feature editActivityIncreaseOfOneWeek() {
    return EditActivity.increaseComparingDurationOf(Duration.ofDays(7));
  }

  Feature editActivityDecreaseOfTwoHours() {
    return EditActivity.decreaseComparingDurationOf(Duration.ofHours(2));
  }

  Feature editActivityDecreaseOfOneDay() {
    return EditActivity.decreaseComparingDurationOf(Duration.ofDays(1));
  }

  Feature editActivityDecreaseOfOneWeek() {
    return EditActivity.decreaseComparingDurationOf(Duration.ofDays(7));
  }
}

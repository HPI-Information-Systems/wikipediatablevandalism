package features.context;

import features.FeaturePack;
import lombok.val;

/**
 * Configure all context features with their metadata, e.g., name.
 */
public class ContextFeatures {

  private static final ContextFeatures INSTANCE = new ContextFeatures();

  private final FeaturePack features;

  private ContextFeatures() {
    val user = new UserFeatureFactory();
    val edit = new EditFeatureFactory();
    val page = new PageFeatureFactory();

    features = FeaturePack.builder()
        // User
        .feature("isContributorAnonymous", user.isContributorAnonymous())
        .feature("isBot", user.isBot())

        // Edit
        .feature("timeOfDay", edit.timeOfDay())
        .feature("localizedTimeOfDay", edit.localizedTimeOfDay())
        .feature("dayOfWeek", edit.dayOfWeek())
        .feature("localizedDayOfWeek", edit.localizedDayOfWeek())
        .feature("isMinorEdit", edit.isMinorEdit())

        // Page
        .feature("hasPreviousSameContributor", page.hasPreviousSameContributor())
        .feature("timeSinceLastArticleEdit", page.timeSinceLastArticleEdit())
        .feature("timeSinceLastArticleEditBySameContributor",
            page.timeSinceLastArticleEditBySameContributor())
        .feature("timeSinceFirstArticleEdit", page.timeSinceFirstArticleEditBySameContributor())
        .feature("revertCount", page.revertCount())
        .feature("prevRevertCount", page.prevRevertCount())
        .feature("editRatio", page.ratioOffAllEditsToContributorEdits())
        .feature("contributorRevertRatio", page.contributorRevertedBeforeInThatArticleRatio())
        .feature("timeSinceReverted", page.timeSinceContributorRevertedBeforeInThatArticle())
        .feature("articleTemperatureAll", page.articleTemperatureAll())
        .feature("articleTemperatureYear", page.articleTemperatureYear())
        .feature("articleTemperatureMonth", page.articleTemperatureMonth())
        .feature("articleTemperatureWeek", page.articleTemperatureWeek())
        .feature("articleTemperatureDay", page.articleTemperatureDay())
        .feature("articleTemperatureHour", page.articleTemperatureHour())
        .feature("articleTemperatureRatio", page.articleTemperatureRatio())

        .feature("authorRank", page.authorRank())
        .feature("authorRankOfLast200Edits", page.authorRankOfLast200Edits())
        .feature("authorRankOneMonth", page.authorRankOfLastMonth())
        .feature("authorRankOfLast200EditsOfOneMonth", page.authorRankOfLast200EditsOfOneMonth())

        .feature("distinctAuthorCountOfLast5Edits", page.distinctAuthorCountOfLast5Edits())
        .feature("distinctAuthorCountOfLast20Edits", page.distinctAuthorCountOfLast20Edits())
        .feature("distinctAuthorCountOfLast80Edits", page.distinctAuthorCountOfLast80Edits())
        .feature("distinctAuthorCountOfLastHour", page.distinctAuthorCountOfLastHour())
        .feature("distinctAuthorCountOfLastTwoHours", page.distinctAuthorCountOfLastTwoHours())
        .feature("distinctAuthorCountOfLast24Hours", page.distinctAuthorCountOfLast24Hours())

        .feature("editActivityIncreaseOfTwoHours", page.editActivityIncreaseOfTwoHours())
        .feature("editActivityIncreaseOfOneDay", page.editActivityIncreaseOfOneDay())
        .feature("editActivityIncreaseOfOneWeek", page.editActivityIncreaseOfOneWeek())

        .feature("editActivityDecreaseOfTwoHours", page.editActivityDecreaseOfTwoHours())
        .feature("editActivityDecreaseOfOneDay", page.editActivityDecreaseOfOneDay())
        .feature("editActivityDecreaseOfOneWeek", page.editActivityDecreaseOfOneWeek())

        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

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
        .feature("authorRank", edit.authorRank())
        .feature("authorRankOfLast200Edits", edit.authorRankOfLast200Edits())
        .feature("authorRankOneMonth", edit.authorRankOfLastMonth())
        .feature("authorRankOfLast200EditsOfOneMonth", edit.authorRankOfLast200EditsOfOneMonth())

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

        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

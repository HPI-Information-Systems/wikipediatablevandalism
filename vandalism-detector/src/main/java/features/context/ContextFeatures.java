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
    val userFeatureFactory = new UserFeatureFactory();
    val editFeatureFactory = new EditFeatureFactory();
    val pageFeatureFactory = new PageFeatureFactory();

    features = FeaturePack.builder()
        // User
        .feature("isContributorAnonymous", userFeatureFactory.isContributorAnonymous())
        .feature("isBot", userFeatureFactory.isBot())

        // Edit
        .feature("timeOfDay", editFeatureFactory.timeOfDay())
        .feature("localizedTimeOfDay", editFeatureFactory.localizedTimeOfDay())
        .feature("dayOfWeek", editFeatureFactory.dayOfWeek())
        .feature("localizedDayOfWeek", editFeatureFactory.localizedDayOfWeek())
        .feature("isMinorEdit", editFeatureFactory.isMinorEdit())
        .feature("authorRank", editFeatureFactory.authorRank())
        .feature("authorRankOfLast200Edits", editFeatureFactory.authorRankOfLast200Edits())
        .feature("authorRankOneMonth", editFeatureFactory.authorRankOfLastMonth())
        .feature("authorRankOfLast200EditsOfOneMonth", editFeatureFactory.authorRankOfLast200EditsOfOneMonth())

        // Page
        .feature("hasPreviousSameContributor", pageFeatureFactory.hasPreviousSameContributor())
        .feature("timeSinceLastArticleEdit", pageFeatureFactory.timeSinceLastArticleEdit())
        .feature("timeSinceLastArticleEditBySameContributor", pageFeatureFactory.timeSinceLastArticleEditBySameContributor())
        .feature("timeSinceFirstArticleEdit", pageFeatureFactory.timeSinceFirstArticleEditBySameContributor())
        .feature("revertRatio", pageFeatureFactory.revertRatio())
        .feature("editRatio", pageFeatureFactory.ratioOffAllEditsToContributorEdits())
        .feature("contributorRevertCount", pageFeatureFactory.contributorRevertedBeforeInThatArticleCount())
        .feature("timeSinceReverted", pageFeatureFactory.timeSinceContributorRevertedBeforeInThatArticle())
        .feature("articleTemperatureAll", pageFeatureFactory.articleTemperatureAll())
        .feature("articleTemperatureYear", pageFeatureFactory.articleTemperatureYear())
        .feature("articleTemperatureMonth", pageFeatureFactory.articleTemperatureMonth())
        .feature("articleTemperatureWeek", pageFeatureFactory.articleTemperatureWeek())
        .feature("articleTemperatureDay", pageFeatureFactory.articleTemperatureDay())
        .feature("articleTemperatureHour", pageFeatureFactory.articleTemperatureHour())

        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

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
        .feature("isContributorAnonymous", userFeatureFactory.isContributorAnonymous())
        .feature("isBot", userFeatureFactory.isBot())
        .feature("timeOfDay", editFeatureFactory.timeOfDay())
        .feature("dayOfWeek", editFeatureFactory.dayOfWeek())
        .feature("isMinorEdit", editFeatureFactory.isMinorEdit())
        .feature("hasPreviousSameContributor", pageFeatureFactory.hasPreviousSameContributor())
        .feature("timeSinceLastArticleEdit", pageFeatureFactory.timeSinceLastArticleEdit())
        .feature("timeSinceLastArticleEditBySameContributor", pageFeatureFactory.timeSinceLastArticleEditBySameContributor())
        .feature("timeSinceFirstArticleEditBySameContributor", pageFeatureFactory.timeSinceFirstArticleEditBySameContributor())
        .feature("revertCount", pageFeatureFactory.revertCount())
        .feature("ratioOffAllEditsToContributorEdits", pageFeatureFactory.ratioOffAllEditsToContributorEdits())
        .feature("contributorRevertedBeforeInThatArticleCount", pageFeatureFactory.contributorRevertedBeforeInThatArticleCount())
        .feature("timeSinceContributorRevertedBeforeInThatArticle", pageFeatureFactory.timeSinceContributorRevertedBeforeInThatArticle())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

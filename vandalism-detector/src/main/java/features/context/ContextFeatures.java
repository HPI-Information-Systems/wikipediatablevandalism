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
    val contextFactory = new ContextFeatureFactory();
    val contextPreviousRevision = new ContextPreviousRevisionFeatureFactory();
    val contextPreviousRevisions = new ContextPreviousRevisionsFeatureFactory();

    features = FeaturePack.builder()
        .feature("isContributorAnonymous", contextFactory.isContributorAnonymous())
        .feature("timeOfDay", contextFactory.timeOfDay())
        .feature("dayOfWeek", contextFactory.dayOfWeek())
        .feature("isMinorEdit", contextFactory.isMinorEdit())
        .feature("commentLength", contextFactory.commentLength())
        .feature("isBot", contextFactory.isBot())
        .feature("hasPreviousSameContributor",
            contextPreviousRevision.hasPreviousSameContributor())
        .feature("timeSinceLastArticleEdit", contextPreviousRevision.timeSinceLastArticleEdit())
        .feature("sizeChange", contextPreviousRevision.sizeChange())
        .feature("timeSinceLastArticleEditBySameContributor",
            contextPreviousRevisions.timeSinceLastArticleEditBySameContributor())
        .feature("revertCount", contextPreviousRevisions.revertCount())
        .feature("ratioOffAllEditsToContributorEdits",
            contextPreviousRevisions.ratioOffAllEditsToContributorEdits())
        .feature("contributorRevertedBeforeInThatArticleCount",
            contextPreviousRevisions.contributorRevertedBeforeInThatArticleCount())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

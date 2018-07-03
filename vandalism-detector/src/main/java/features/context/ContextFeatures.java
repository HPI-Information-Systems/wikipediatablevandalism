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
    val contextFutureFactory = new ContextFutureFeatureFactory();
    val contextPreviousRevision = new ContextPreviousRevisionFeatureFactory();
    val contextPreviousRevisions = new ContextPreviousRevisionsFeatureFactory();

    features = FeaturePack.builder()
        .feature("is_contributor_anonymous", contextFactory.isContributorAnonymous())
        .feature("time_of_day", contextFactory.timeOfDay())
        .feature("day_of_week", contextFactory.dayOfWeek())
        .feature("is_minor_edit", contextFactory.isMinorEdit())
        .feature("comment_length", contextFactory.commentLength())
        .feature("is_bot", contextFactory.isBot())
        //.feature("is_comment_deleted", contextFutureFactory.isCommentDeleted())
        //.feature("is_contributor_deleted", contextFutureFactory.isContributorDeleted())
        .feature("is_previous_same_contributor",
            contextPreviousRevision.isPreviousSameContributor())
        .feature("time_since_last_article_edit", contextPreviousRevision.timeSinceLastArticleEdit())
        .feature("size_change", contextPreviousRevision.sizeChange())
        .feature("size_ratio", contextPreviousRevision.sizeRatio())
        .feature("time_since_last_article_edit_by_same_contributor",
            contextPreviousRevisions.timeSinceLastArticleEditBySameContributor())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

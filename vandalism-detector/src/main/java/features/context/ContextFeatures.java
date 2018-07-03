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
    val contextDeltaFactory = new ContextDeltaFeatureFactory();

    features = FeaturePack.builder()
        .feature("is_contributor_anonymous", contextFactory.isContributorAnonymous())
        .feature("time_of_day", contextFactory.timeOfDay())
        .feature("day_of_week", contextFactory.dayOfWeek())
        .feature("is_minor_edit", contextFactory.isMinorEdit())
        .feature("comment_length", contextFactory.commentLength())
        .feature("is_previous_same_contributor", contextDeltaFactory.isPreviousSameContributor())
        .feature("time_since_last_article_edit", contextDeltaFactory.timeSinceLastArticleEdit())
        .feature("size_change", contextDeltaFactory.sizeChange())
        .feature("size_ratio", contextDeltaFactory.sizeRatio())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

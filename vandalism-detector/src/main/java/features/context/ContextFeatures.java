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
    val factory = new ContextFeatureFactory();

    features = FeaturePack.builder()
        .feature("anonymous", factory.isAnonymous())
        .feature("user_deleted", factory.isContributorDeleted())
        .feature("created_time_of_day", factory.createdTimeOfDay())
        .feature("created_day_of_week", factory.createdDayOfWeek())
        .feature("is_minor_edit", factory.isMinorEdit())
        .feature("comment_length", factory.commentLength())
        .feature("comment_deleted", factory.isCommentDeleted())
        .build();
  }

  public FeaturePack getFeatures() {
    return features;
  }

  public static ContextFeatures get() {
    return INSTANCE;
  }
}

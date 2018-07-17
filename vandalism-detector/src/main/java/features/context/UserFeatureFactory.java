package features.context;

import features.Feature;
import features.context.util.BotList;
import lombok.val;
import util.BasicUtils;

/**
 * Create all features based on the metadata connected to the current contributor
 */
class UserFeatureFactory {

  Feature isContributorAnonymous() {
    return (revision, ignored) -> revision.getContributor().getUsername() == null;
  }

  Feature isBot() {
    val botList = BotList.read();
    return (revision, ignored) -> {

      if (BasicUtils.isAnonymous(revision.getContributor())) {
        return false;
      }

      return botList.stream().anyMatch(str -> str.equals(revision.getContributor().getUsername()));
    };
  }

  // future feature currently not used
  Feature isContributorDeleted() {
    return (revision, featureContext) -> revision.getContributor().getDeleted() != null;
  }

}

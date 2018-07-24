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
    return parameters -> parameters.getRevision().getContributor().getUsername() == null;
  }

  Feature isBot() {
    val botList = BotList.read();
    return parameters -> {

      if (BasicUtils.isAnonymous(parameters.getRevision().getContributor())) {
        return false;
      }

      return botList.stream().anyMatch(str -> str.equals(parameters.getRevision().getContributor().getUsername()));
    };
  }

  // future feature currently not used
  Feature isContributorDeleted() {
    return parameters -> parameters.getRevision().getContributor().getDeleted() != null;
  }

}

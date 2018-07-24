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
    return parameters -> parameters.getRevision().getContributor().getUsername() == null
        ? 1 : 0;
  }

  Feature isBot() {
    val botList = BotList.read();
    return parameters -> {

      if (BasicUtils.isAnonymous(parameters.getRevision().getContributor())) {
        return 0;
      }

      return botList.stream().anyMatch(str -> str.equals(parameters.getRevision().getContributor().getUsername()))
          ? 1 : 0;
    };
  }

  // future feature currently not used
  Feature isContributorDeleted() {
    return parameters -> parameters.getRevision().getContributor().getDeleted() != null
        ? 1 : 0;
  }
}

package features.context.util;

import lombok.val;
import model.FeatureParameters;
import util.BasicUtils;

public class ArticleTemperature {

  //everything in minutes
  private static double MIN = 1;
  private static double HOUR = MIN * 60;
  private static double DAY = HOUR * 25;
  private static double WEEK = DAY * 7;
  private static double MONTH = DAY * 30;
  private static double YEAR = MONTH * 12;

  private static double editsPer = HOUR;

  public static double getAll(FeatureParameters parameters) {
    val previousRevisions = parameters.getPreviousRevisions();
    val duration = BasicUtils.getTimeDuration(parameters.getRevision(), previousRevisions.get(previousRevisions.size()-1)); // return minutes
    return previousRevisions.size() / (duration / editsPer);
  }

  public static double getYear(FeatureParameters parameters) {
    return get(parameters, YEAR);
  }

  public static double getMonth(FeatureParameters parameters) {
    return get(parameters, MONTH);
  }

  public static double getWeek(FeatureParameters parameters) {
    return get(parameters, WEEK);
  }

  public static double getDay(FeatureParameters parameters) {
    return get(parameters, DAY);
  }

  public static double getHour(FeatureParameters parameters) {
    return get(parameters, HOUR);
  }

  /**
   * the amount of edits per hour in distinct duration
   */
  private static double get(FeatureParameters parameters, double maxDuration) {
      int editCount = 0;
      val previousRevisions = parameters.getPreviousRevisions();
      for (val previousRevision : previousRevisions) {
        val duration = BasicUtils.getTimeDuration(parameters.getRevision(), previousRevision);
        if (duration > maxDuration) {
          break;
        }
        ++editCount;
      }
      return editCount / (maxDuration / editsPer);
  }

}

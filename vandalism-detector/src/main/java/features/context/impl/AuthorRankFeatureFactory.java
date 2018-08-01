package features.context.impl;

import features.Feature;
import java.time.Period;

public class AuthorRankFeatureFactory {

  public Feature authorRank() {
    return new AuthorRank(RevisionProvider.all());
  }

  public Feature authorRankOfLast200Edits() {
    return new AuthorRank(RevisionProvider.lastN(200));
  }

  public Feature authorRankOfLastMonth() {
    return new AuthorRank(RevisionProvider.maxAge(Period.ofDays(30)));
  }

  public Feature authorRankOfLast200EditsOfOneMonth() {
    return new AuthorRank(RevisionProvider.lastNWithMaxAge(200, Period.ofDays(30)));
  }
}

package features.content;

import features.Feature;
import lombok.RequiredArgsConstructor;
import matching.MatchService;

@RequiredArgsConstructor
public class ContentFeatureFactory {

  private final MatchService matchService;

  Feature geometryChange() {
    return new TableGeometry(matchService);
  }

}

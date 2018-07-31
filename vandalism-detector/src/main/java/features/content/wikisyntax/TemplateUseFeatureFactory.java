package features.content.wikisyntax;

import features.Feature;

class TemplateUseFeatureFactory {

  Feature pageLinkChanges() {
    return new WikiLinkChange();
  }

  Feature yesNoChanges() {
    return new YesNo();
  }

  Feature fifaFlagChanges() {
    return new FootballFlags();
  }

  Feature flagChanges() {
    return new Flags();
  }
}

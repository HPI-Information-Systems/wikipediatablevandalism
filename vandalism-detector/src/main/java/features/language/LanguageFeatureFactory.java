package features.language;

import features.Feature;

class LanguageFeatureFactory {

  Feature offensiveWordsInComment() {
    return new OffensiveWordsInComment();
  }

  Feature offensiveWordsInTable() {
    return new OffensiveWordsInTable();
  }

  Feature personalPronounsInComment() {
    return new PronounFrequencyInComment();
  }

  Feature tablePersonalPronounFrequency() {
    return new TablePronounFrequency();
  }

  Feature tablePersonalPronounImpact() {
    return new TablePronounImpact();
  }

  Feature addedNonDictionaryWordCount() {
    return new AddedNonDictionaryWords();
  }

}

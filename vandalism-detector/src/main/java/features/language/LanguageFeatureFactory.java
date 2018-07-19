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

  Feature personalPronounsInTable() {
    return new TablePronounFrequency();
  }

  Feature addedNonDictionaryWordCount() {
    return new AddedNonDictionaryWords();
  }

}

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
    return new PersonalPronounsInComment();
  }

  Feature personalPronounsInTable() {
    return new PersonalPronounsInTable();
  }

  Feature addedNonDictionaryWordCount() {
    return new AddedNonDictionaryWords();
  }

}

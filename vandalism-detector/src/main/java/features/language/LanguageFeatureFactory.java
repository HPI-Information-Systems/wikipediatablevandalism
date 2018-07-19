package features.language;

import features.Feature;

class LanguageFeatureFactory {

  Feature offensiveWordsInComment() {
    return new OffensiveWordsInComment();
  }

  Feature offensiveWordsInTable() {
    return new OffensiveWordsInTable();
  }

  Feature commentPersonalPronounFrequency() {
    return new CommentPronounFrequency();
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

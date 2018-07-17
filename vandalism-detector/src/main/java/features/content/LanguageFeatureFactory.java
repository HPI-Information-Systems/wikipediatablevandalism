package features.content;

import features.Feature;
import features.content.util.language.AddedNonDictionaryWords;
import features.content.util.language.OffensiveWordsInComment;
import features.content.util.language.OffensiveWordsInTable;

class LanguageFeatureFactory {

  Feature offensiveWordsInComment() {
    return new OffensiveWordsInComment();
  }

  Feature offensiveWordsInTable() {
    return new OffensiveWordsInTable();
  }

  Feature addedNonDictionaryWordCount() {
    return new AddedNonDictionaryWords();
  }

}

package features.language;

import features.Feature;
import features.language.wordlists.DictionaryWordList;
import features.language.wordlists.OffensiveWordList;
import features.language.wordlists.PronounWordList;

class LanguageFeatureFactory {

  Feature personalPronounFrequencyInComment() {
    return new CommentWordFrequency(PronounWordList.getWords());
  }

  Feature personalPronounFrequencyInTable() {
    return new TableWordFrequency(PronounWordList.getWords());
  }

  Feature personalPronounImpactInTable() {
    return new TableWordImpact(PronounWordList.getWords());
  }

  Feature offensiveWordFrequencyInComment() {
    return new CommentWordFrequency(OffensiveWordList.getWords());
  }

  Feature offensiveWordFrequencyInTables() {
    return new TableWordFrequency(OffensiveWordList.getWords());
  }

  Feature offensiveWordImpactInTables() {
    return new TableWordImpact(OffensiveWordList.getWords());
  }

  Feature nonDictionaryWordFrequencyInComment() {
    return new CommentWordFrequency(DictionaryWordList.getWords(), false);
  }

  Feature nonDictionaryWordFrequencyInTable() {
    return new TableWordFrequency(DictionaryWordList.getWords(), false);
  }

  Feature nonDictionaryWordImpactInTable() {
    return new TableWordImpact(DictionaryWordList.getWords(), false);
  }
}

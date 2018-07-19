package features.language;

import features.Feature;
import features.language.wordlists.DictionaryWordList;
import features.language.wordlists.OffensiveWordList;
import features.language.wordlists.PronounWordList;

class LanguageFeatureFactory {

  Feature commentPersonalPronounFrequency() {
    return new CommentWordFrequency(PronounWordList.getWords());
  }

  Feature commentNonDictionaryWordFrequency() {
    return new CommentWordFrequency(DictionaryWordList.getWords(), false);
  }

  Feature commentOffensiveWordFrequency() {
    return new CommentWordFrequency(OffensiveWordList.getWords());
  }

  Feature tablePersonalPronounFrequency() {
    return new TableWordFrequency(PronounWordList.getWords());
  }

  Feature tablePersonalPronounImpact() {
    return new TableWordImpact(PronounWordList.getWords());
  }

  Feature tableOffensiveWordFrequency() {
    return new TableWordFrequency(OffensiveWordList.getWords());
  }

  Feature tableOffensiveWordImpact() {
    return new TableWordImpact(OffensiveWordList.getWords());
  }

  Feature tableNonDictionaryWordFrequency() {
    return new TableWordFrequency(DictionaryWordList.getWords(), false);
  }

  Feature tableNonDictionaryWordImpact() {
    return new TableWordImpact(DictionaryWordList.getWords(), false);
  }
}

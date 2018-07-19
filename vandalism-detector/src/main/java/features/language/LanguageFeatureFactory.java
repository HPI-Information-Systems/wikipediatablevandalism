package features.language;

import features.Feature;
import features.language.wordlists.OffensiveWordList;
import features.language.wordlists.PronounWordList;

class LanguageFeatureFactory {

  Feature commentPersonalPronounFrequency() {
    return new CommentWordFrequency(PronounWordList.getWords());
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

  Feature addedNonDictionaryWordCount() {
    return new AddedNonDictionaryWords();
  }

}

package features.language;

import features.Feature;
import features.language.regex.SuperlativePatternList;
import features.language.wordlists.DictionaryWordList;
import features.language.wordlists.PronounWordList;
import features.language.wordlists.RevertWordList;
import features.language.wordlists.SexualWordList;
import features.language.wordlists.VulgarWordList;

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

  Feature vulgarWordFrequencyInComment() {
    return new CommentWordFrequency(VulgarWordList.getWords());
  }

  Feature vulgarWordFrequencyInTables() {
    return new TableWordFrequency(VulgarWordList.getWords());
  }

  Feature vulgarWordImpactInTables() {
    return new TableWordImpact(VulgarWordList.getWords());
  }

  Feature sexualWordFrequencyInComment() {
    return new CommentWordFrequency(SexualWordList.getWords());
  }

  Feature sexualWordFrequencyInTables() {
    return new TableWordFrequency(SexualWordList.getWords());
  }

  Feature sexualWordImpactInTables() {
    return new TableWordImpact(SexualWordList.getWords());
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

  public Feature superlativeWordFrequencyInTable() {
    return new TableRegexFrequency(SuperlativePatternList.getPatterns());
  }

  public Feature superlativeWordImpactInTable() {
    return new TableRegexImpact(SuperlativePatternList.getPatterns());
  }

  public Feature superlativeWordFrequencyInComment() {
    return new CommentRegexFrequency(SuperlativePatternList.getPatterns());
  }

  Feature revertInComment() {
    return new CommentContainsWords(RevertWordList.getWords());
  }
}

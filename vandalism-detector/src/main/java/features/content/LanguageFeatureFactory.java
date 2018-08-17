package features.content;

import com.google.common.collect.Lists;
import features.Feature;
import features.content.util.language.AverageFeature;
import features.content.util.language.CommentContainsWords;
import features.content.util.language.CommentRegexFrequency;
import features.content.util.language.CommentWordFrequency;
import features.content.util.language.TableCellRegexFrequency;
import features.content.util.language.TableCellRegexImpact;
import features.content.util.language.TableRegexFrequency;
import features.content.util.language.TableRegexImpact;
import features.content.util.language.TableWordFrequency;
import features.content.util.language.TableWordImpact;
import features.content.util.language.regex.SuperlativePatternList;
import features.content.util.language.regex.WikiSyntaxPatternList;
import features.content.util.language.wordlists.DictionaryWordList;
import features.content.util.language.wordlists.GoodFaithWordList;
import features.content.util.language.wordlists.PronounWordList;
import features.content.util.language.wordlists.RedirectWordList;
import features.content.util.language.wordlists.ReplaceWordList;
import features.content.util.language.wordlists.RevertWordList;
import features.content.util.language.wordlists.SexualWordList;
import features.content.util.language.wordlists.VulgarWordList;

class LanguageFeatureFactory {

  Feature personalPronounFrequencyInComment() {
    return CommentWordFrequency.ofMatchedWords(PronounWordList.getWords()).stem(false).build();
  }

  Feature personalPronounFrequencyInTable() {
    return TableWordFrequency.ofMatchedWords(PronounWordList.getWords()).stem(false).build();
  }

  Feature personalPronounImpactInTable() {
    return TableWordImpact.ofMatchedWords(PronounWordList.getWords()).stem(false).build();
  }

  Feature vulgarWordFrequencyInComment() {
    return CommentWordFrequency.ofMatchedWords(VulgarWordList.getWords()).build();
  }

  Feature vulgarWordFrequencyInTables() {
    return TableWordFrequency.ofMatchedWords(VulgarWordList.getWords()).build();
  }

  Feature vulgarWordImpactInTables() {
    return TableWordImpact.ofMatchedWords(VulgarWordList.getWords()).build();
  }

  Feature sexualWordFrequencyInComment() {
    return CommentWordFrequency.ofMatchedWords(SexualWordList.getWords()).build();
  }

  Feature sexualWordFrequencyInTables() {
    return TableWordFrequency.ofMatchedWords(SexualWordList.getWords()).build();
  }

  Feature sexualWordImpactInTables() {
    return TableWordImpact.ofMatchedWords(SexualWordList.getWords()).build();
  }

  Feature nonDictionaryWordFrequencyInComment() {
    return CommentWordFrequency.ofUnmatchedWords(DictionaryWordList.getWords()).build();
  }

  Feature nonDictionaryWordFrequencyInTable() {
    return TableWordFrequency.ofUnmatchedWords(DictionaryWordList.getWords()).build();
  }

  Feature nonDictionaryWordImpactInTable() {
    return TableWordImpact.ofUnmatchedWords(DictionaryWordList.getWords()).build();
  }

  Feature wikiSyntaxElementFrequencyInTable() {
    return new TableCellRegexFrequency(WikiSyntaxPatternList.getPatterns());
  }

  Feature wikiSyntaxElementImpactInTable() {
    return new TableCellRegexImpact(WikiSyntaxPatternList.getPatterns());
  }

  Feature wikiSyntaxElementFrequencyInComment() {
    return new CommentRegexFrequency(WikiSyntaxPatternList.getPatterns());
  }

  Feature averageAllBadWordFrequencyInTable() {
    return new AverageFeature(Lists.newArrayList(
        vulgarWordFrequencyInTables(),
        personalPronounFrequencyInTable(),
        nonDictionaryWordFrequencyInTable(),
        sexualWordFrequencyInTables(),
        superlativeWordFrequencyInTable()
    ));
  }

  Feature averageAllBadWordImpactInTable() {
    return new AverageFeature(Lists.newArrayList(
        vulgarWordImpactInTables(),
        personalPronounImpactInTable(),
        nonDictionaryWordImpactInTable(),
        sexualWordImpactInTables(),
        superlativeWordImpactInTable()
    ));
  }

  Feature averageAllBadWordFrequencyInComment() {
    return new AverageFeature(Lists.newArrayList(
        vulgarWordFrequencyInComment(),
        personalPronounFrequencyInComment(),
        nonDictionaryWordFrequencyInComment(),
        sexualWordFrequencyInComment(),
        superlativeWordFrequencyInComment()
    ));
  }

  Feature superlativeWordFrequencyInTable() {
    return new TableRegexFrequency(SuperlativePatternList.getPatterns());
  }

  Feature superlativeWordImpactInTable() {
    return new TableRegexImpact(SuperlativePatternList.getPatterns());
  }

  Feature superlativeWordFrequencyInComment() {
    return new CommentRegexFrequency(SuperlativePatternList.getPatterns());
  }

  Feature revertInComment() {
    return new CommentContainsWords(RevertWordList.getWords());
  }

  Feature redirectInComment() {
    return new CommentContainsWords(RedirectWordList.getWords());
  }

  Feature replaceInComment() {
    return new CommentContainsWords(ReplaceWordList.getWords());
  }

  Feature goodFaithInComment() {
    return new CommentContainsWords(GoodFaithWordList.getWords());
  }


  Feature personalPronounInComment() {
    return new CommentContainsWords(PronounWordList.getWords());
  }

}

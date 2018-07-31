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
import features.content.util.language.wordlists.PronounWordList;
import features.content.util.language.wordlists.RedirectWordList;
import features.content.util.language.wordlists.RevertWordList;
import features.content.util.language.wordlists.SexualWordList;
import features.content.util.language.wordlists.VulgarWordList;

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
}

package features.content.util.table;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.Test;

class SyntaxCheckerTest {

  private String testContent =
      "This is a list of American district / county / state's attorneys by state and county / judicial district\n"
          + "\n"
          + "<br clear=\"all\">\n"
          + "{|  border=\"1\" cellpadding=\"2\" cellspacing=\"0\"\n"
          + "|- bgcolor=lightgrey\n"
          + "! State\n"
          + "! County / Judicial District (County)\n"
          + "! District / County / State's Attorney\n"
          + "|-\n"
          + "| [[Alabama]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Alaska]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Arizona]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "|\n"
          + "| [[Apache County, Arizona|Apache]] ||[[Criss Candelaria]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Cochise County, Arizona|Cochise]] ||[[Edward G. Rheinheimer]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Coconino County, Arizona|Coconino]] ||[[Terence C. Hance]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Gila County, Arizona|Gila]] ||[[Daisy Flores]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Graham County, Arizona|Graham]] ||[[Kenneth A. Angle]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Greenlee County, Arizona|Greenlee]] ||[[Derek D. Rapier]]\n"
          + "|-\n"
          + "|\n"
          + "| [[La Paz County, Arizona|La Paz]] ||[[Martin Brannan]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Maricopa County, Arizona|Maricopa]] ||[[Andrew Thomas]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Mohave County, Arizona|Mohave]] ||[[Matthew Smith]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Navajo County, Arizona|Navajo]] ||[[Melvin R. Bowers, Jr.]]\n"
          + "|-\n"
          + "| \n"
          + "| [[Pima County, Arizona|Pima]] ||[[Barbara LaWall]] \n"
          + "|-\n"
          + "|\n"
          + "| [[Pinal County, Arizona|Pinal]] ||[[Robert Carter Olson]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Santa Cruz County, Arizona|Santa Cruz]] ||[[George E. Silva]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Yavapai County, Arizona|Yavapai]] ||[[Sheila S. Polk]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Yuma County, Arizona|Yuma]] ||[[Jon R. Smith]]\n"
          + "|-\n"
          + "| [[California]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "|\n"
          + "| [[Alameda County, California|Alameda]] ||[[Thomas Orloff]] \n"
          + "|-\n"
          + "|\n"
          + "| [[Alpine County, California|Alpine]] ||[[William Richmond]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Butte County, California|Butte]] ||[[Michael Ramsey]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Calaveras County, California|Calaveras]] ||[[Jeffrey Tuttle]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Colusa County, California|Colusa]] ||[[John Poyner]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Contra Costa County, California|Contra Costa]] ||[[Robert Kochly]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Del Norte County, California|Del Norte]] ||[[Michael Riese]]\n"
          + "|-\n"
          + "|\n"
          + "| [[El Dorado County, California|El Dorado]] ||[[Gary Lacy]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Fresno County, California|Fresno]] ||[[Elizabeth Egan]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Glenn County, California|Glenn]] ||[[Robert Holzapfel]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Humboldt County, California|Humboldt]] ||[[Paul \"plagiarist\"[http://watchpaul-articles.blogspot.com/2006/11/ts-paul-gallegos-my-word-vigilantism.htmlGallegos]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Imperial County, California|Imperial]] ||[[Gilbert Otero]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Inyo County, California|Inyo]] ||[[Arthur Maillet]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Kern County, California|Kern]] ||[[Edward Jagels]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Kings County, California|King]] ||[[Ronald Calhoun]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Lake County, California|Lake]] ||[[Gerhard Luck]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Lassen County, California|Lassen]] ||[[Robert Burns]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Los Angeles County, California|Los Angeles]] ||[[Steve Cooley]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Madera County, California|Madera]] ||[[Ernest LiCalsi]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Marin County, California|Marin]] ||[[Edward Berberian]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Mariposa County, California|Mariposa]] ||[[Robert Brown]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Mendocino County, California|Mendocino]] ||[[Norman Vroman]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Merced County, California|Merced]] ||[[Gordon Spencer]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Modoc County, California|Modoc]] ||[[Jordan Funk]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Mono County, California|Mono]] ||[[George Booth]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Monterey County, California|Monterey]] ||[[Dean Flippo]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Napa County, California|Napa]] ||[[Gary Lieberstein]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Nevada County, California|Nevada]] ||[[Michael Ferguson]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Orange County, California|Orange]] ||[[Tony Rackauckas]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Placer County, California|Placer]] ||[[Bradford Fenocchio]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Plumas County, California|Plumas]] ||[[Jeff Cunan]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Riverside County, California|Riverside]] ||[[Grover Trask II]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sacramento County, California|Sacramento]] || [[Jan Scully]]\n"
          + "|-\n"
          + "|\n"
          + "| [[San Benito County, California|San Benito]] ||[[John Sarsfield]]\n"
          + "|-\n"
          + "|\n"
          + "| [[San Bernardino County, California|San Bernardino]] ||[[Michael Ra]]mos\n"
          + "|-\n"
          + "|\n"
          + "| [[San Diego County, California|San Diego]] ||[[Bonnie Dumanis]]\n"
          + "|-\n"
          + "|\n"
          + "| [[San Francisco County, California|San Francisco]] ||[[Kamala Harris]]\n"
          + "|-\n"
          + "|\n"
          + "| [[San Joaquin County, California|San Joaquin]] ||[[John Phillips]]\n"
          + "|-\n"
          + "|\n"
          + "| [[San Luis Obispo County, California|San Luis Obispo]] ||[[Gerald Shea]]\n"
          + "|-\n"
          + "|\n"
          + "| [[San Mateo County, California|San Mateo]] ||[[James Fox]] \n"
          + "|-\n"
          + "|\n"
          + "| [[Santa Barbara County, California|Santa Barbara]] ||[[Thomas W. Sneddon Jr.]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Santa Clara County, California|Santa Clara]] ||[[George Kennedy]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Santa Cruz County, California|Santa Cruz]] ||[[Bob Lee (District attorney)|Bob Lee]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Shasta County, California|Shasta]] ||[[Gerald Benito]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sierra County, California|Sierra]] || [[Lawrence Allen]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Siskiyou County, California|Siskiyou]] ||[[James Andrus]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Solano County, California|Solano]] ||[[David Paulson]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sonoma County, California|Sonoma]] ||[[Stephan Passalacqua]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Stanislaus County, California|Stanislaus]] ||[[James Brazelton]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sutter County, California|Sutter]] ||[[Carl Adams]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Tehama County, California|Tehama]] ||[[Gregg Cohen]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Trinity County, California|Trinity]] ||[[David Cross]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Tulare County, California|Tulare]] ||[[Phillip Cline]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Tuolumne County, California|Tuolumne]] ||[[Donald Segerstrom, Jr]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Ventura County, California|Ventura]] ||[[Gregory Totten]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Yolo County, California|Yolo]] ||[[David Henderson]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Yuba County, California|Yuba]] || [[Patrick McGrath]]\n"
          + "|-\n"
          + "| [[Colorado]]\n"
          + "|  ||\n"
          + "|-\n"
          + "|\n"
          + "| 1st ([[Gilpin County, Colorado|Gilpin]] and [[Jefferson County, Colorado|Jefferson]]) ||[[Scott Storey]]\n"
          + "|-\n"
          + "|\n"
          + "| 2nd ([[Denver, Colorado|Denver]]) ||[[Mitch Morrissey]]\n"
          + "|-\n"
          + "|\n"
          + "| 3rd ([[Huerfano County, Colorado|Huerfano]] and [[Las Animas County, Colorado|Las Animas]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 4th ([[El Paso County, Colorado|El Paso]] and [[Teller County, Colorado|Teller]]) ||[[John Newsome]]\n"
          + "|-\n"
          + "|\n"
          + "| 5th ([[Clear Creek County, Colorado|Clear Creek]], [[Eagle County, Colorado|Eagle]], [[Lake County, Colorado|Lake]], and [[Summit County, Colorado|Summit]]) ||[[Mark Hurlbert]]\n"
          + "|-\n"
          + "|\n"
          + "| 6th ([[Archuleta County, Colorado|Archuleta]], [[La Plata County, Colorado|La Plata]], and [[San Juan County, Colorado|San Juan]]) ||[[Craig Westberg]]\n"
          + "|-\n"
          + "|\n"
          + "| 7th ([[Delta County, Colorado|Delta]], [[Gunnison County, Colorado|Gunnison]], [[Hinsdale County, Colorado|Hinsdale]], [[Montrose County, Colorado|Montrose]], [[Ouray County, Colorado|Ouray]], and [[San Miguel County, Colorado|San Miguel]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 8th ([[Jackson County, Colorado|Jackson]] and [[Larimer County, Colorado|Larimer]]) ||[[Larry Abrahamson]]\n"
          + "|-\n"
          + "|\n"
          + "| 9th ([[Garfield County, Colorado|Garfield]], [[Rio Blanco County, Colorado|Rio Blanco]], and [[Pitkin County, Colorado|Pitkin]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 10th ([[Pueblo County, Colorado|Pueblo]]) ||[[Bill Thiebaut]]\n"
          + "|-\n"
          + "|\n"
          + "| 11th ([[Chaffee County, Colorado|Chaffee]], [[Custer County, Colorado|Custer]], [[Fremont County, Colorado|Fremont]], and [[Park County, Colorado|Park]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 12th ([[Alamosa County, Colorado|Alamosa]], [[Conejos County, Colorado|Conejos]], [[Costilla County, Colorado|Costilla]], [[Mineral County, Colorado|Mineral]], [[Rio Grande County, Colorado|Rio Grande]], and [[Saguache County, Colorado|Saguache]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 13th ([[Kit Carson County, Colorado|Kit Carson]], [[Logan County, Colorado|Logan]], [[Morgan County, Colorado|Morgan]], [[Phillips County, Colorado|Phillips]], [[Sedgwick County, Colorado|Sedgwick]], [[Washington County, Colorado|Washington]], and [[Yuma County, Colorado|Yuma]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 14th ([[Grand County, Colorado|Grand]], [[Moffat County, Colorado|Moffat]], and [[Routt County, Colorado|Routt]]) ||[[Bonnie Roesink]]\n"
          + "|-\n"
          + "|\n"
          + "| 15th ([[Baca County, Colorado|Baca]], [[Cheyenne County, Colorado|Cheyenne]], [[Kiowa County, Colorado|Kiowa]], and [[Prowers County, Colorado|Prowers]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 16th ([[Bent County, Colorado|Bent]], [[Crowley County, Colorado|Crowley]], and [[Otero County, Colorado|Otero]]) ||\n"
          + "|-\n"
          + "|\n"
          + "| 17th ([[Adams County, Colorado|Adams]] and [[Broomfield, Colorado|Broomfield]]) ||[[Don Quick]]\n"
          + "|-\n"
          + "|\n"
          + "| 18th ([[Arapahoe County, Colorado|Arapahoe]], [[Douglas County, Colorado|Douglas]], [[Elbert County, Colorado|Elbert]], and [[Lincoln County, Colorado|Lincoln]]) ||[[Carol Chambers]]\n"
          + "|-\n"
          + "|\n"
          + "| 19th ([[Weld County, Colorado|Weld]]) ||[[Kenneth Buck]]\n"
          + "|-\n"
          + "|\n"
          + "| 20th ([[Boulder County, Colorado|Boulder]]) ||[[Mary Lacy]]\n"
          + "|-\n"
          + "|\n"
          + "| 21st ([[Mesa County, Colorado|Mesa]]) ||[[Pete Hautzinger]]\n"
          + "|-\n"
          + "|\n"
          + "| 22nd ([[Dolores County, Colorado|Dolores]] and [[Montezuma County, Colorado|Montezuma]]) ||\n"
          + "|-\n"
          + "| [[Connecticut]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Delaware]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Florida]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Georgia (U.S. state)|Georgia]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Hawaii]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[Idaho]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "| [[New York]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "|\n"
          + "| [[Nassau County, New York|Nassau]] || [[Kathleen Rice]]\n"
          + "|-\n"
          + "|\n"
          + "| [[New York County, New York|New York]] || [[Robert Morgenthau]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Queens, New York|Queens]] || [[Richard A. Brown]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Suffolk County, New York|Suffolk]] || [[Thomas Spota]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Westchester, New York|Westchester]] || [[Janet DiFiore]]\n"
          + "|-\n"
          + "\n"
          + "| [[Pennsylvania]]\n"
          + "|  ||\n"
          + "|-\n"
          + "|\n"
          + "| [[Philadelphia, Pennsylvania|Philadelphia]] || [[Lynne Abraham]]\n"
          + "|-\n"
          + "| [[Texas]]\n"
          + "|  ||  \n"
          + "|-\n"
          + "|\n"
          + "| [[Bexar County, Texas|Bexar]] || [[Susan Reed]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Travis County, Texas|Travis]] || [[Ronnie Earle]]\n"
          + "|-\n"
          + "| [[Wyoming]]\n"
          + "| ||\n"
          + "|-\n"
          + "|\n"
          + "| [[Albany County, Wyoming|Albany]] ||[[Richard Bohling]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Big Horn County, Wyoming|Big Horn]] ||[[Michelle Burns]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Campbell County, Wyoming|Campbell]] ||[[Jeani Stone]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Carbon County, Wyoming|Carbon]] ||[[David Clark]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Converse County, Wyoming|Converse]] ||[[Quentin Richardson]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Crook County, Wyoming|Crook]] ||[[Joseph Baron]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Fremont County, Wyoming|Fremont]] ||[[Norman Young]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Goshen County, Wyoming|Goshen]] ||[[Patrick Korell]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Hot Springs County, Wyoming|Hot Springs]] ||[[Dan Caldwell]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Johnson County, Wyoming|Johnson]] ||[[Christopher Wages]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Laramie County, Wyoming|Laramie]] ||[[Jon Forwood]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Lincoln County, Wyoming|Lincoln]] ||[[Scott Sargeant]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Natrona County, Wyoming|Natrona]] ||[[Michael Blonigan]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Niobrara County, Wyoming|Niobrara]] ||[[Doyle Davies]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Park County, Wyoming|Park]] ||[[Bryan Skoric]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Platte County, Wyoming|Platte]] ||[[Eric Alden]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sheridan County, Wyoming|Sheridan]] ||[[Matthew Redle]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sublette County, Wyoming|Sublette]] ||[[Van Graham]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Sweetwater County, Wyoming|Sweetwater]] ||[[Harold Moneyhun]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Teton County, Wyoming|Teton]] ||[[Stephen Weichman]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Uinta County, Wyoming|Uinta]] ||[[Mike Greer]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Washakie County, Wyoming|Washakie]] ||[[Albert Sinn]]\n"
          + "|-\n"
          + "|\n"
          + "| [[Weston County, Wyoming|Weston]] ||[[Don Hansen]]\n"
          + "|}\n"
          + "\n"
          + "[[Category:District attorneys]]\n";

  @Test
  void findWikiTableOpen() {
    val matcher = SyntaxChecker.TABLE_OPEN_WIKI.matcher("{|");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findWikiTableClose() {
    val matcher = SyntaxChecker.TABLE_CLOSE_WIKI.matcher("|}");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHTMLTableOpen() {
    val matcher = SyntaxChecker.TABLE_OPEN_HTML.matcher("<table style=\"width:100%\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHTMLTableClose() {
    val matcher = SyntaxChecker.TABLE_CLOSE_HTML.matcher("</table>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void testCheckTableClipCount() {
    val clipCount = SyntaxChecker.tableClipRatio(testContent);
    assertThat(clipCount).isEqualTo(1);
  }

  @Test
  void findHTMLRefOpen() {
    val matcher = SyntaxChecker.REF_HTML_OPEN.matcher("<ref name=\"Chomsky\">");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHTMLRefClose() {
    val matcher = SyntaxChecker.REF_HTML_CLOSE.matcher("</ref>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findWikiRefOpen() {
    val matcher = SyntaxChecker.REF_WIKI_OPEN.matcher("[[ref:");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findWikiRefClose() {
    val matcher = SyntaxChecker.REF_WIKI_CLOSE.matcher("]]");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findLinkOpen() {
    val matcher = SyntaxChecker.LINK_OPEN.matcher("[[");
    assertThat(matcher.find()).isTrue();
  }

  /*
  @Test
  void findLinkClose() {
    val matcher = SyntaxChecker.LINK_CLOSE.matcher("]]");
    assertThat(matcher.find()).isTrue();
  }
  */

  @Test
  void findBoldItalic() {
    val matcher = SyntaxChecker.BOLD_ITALIC.matcher("''");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMathOpen() {
    val matcher = SyntaxChecker.MATH_FORMULA_OPEN.matcher("<math>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findMathClose() {
    val matcher = SyntaxChecker.MATH_FORMULA_CLOSE.matcher("</math>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSuperscriptOpen() {
    val matcher = SyntaxChecker.SUPERSCRIPT_OPEN.matcher("<sup>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSuperscriptClose() {
    val matcher = SyntaxChecker.SUPERSCRIPT_CLOSE.matcher("</sup>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubscriptOpen() {
    val matcher = SyntaxChecker.SUBSCRIPT_OPEN.matcher("<sub>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findSubscriptClose() {
    val matcher = SyntaxChecker.SUBSCRIPT_CLOSE.matcher("</sub>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPoemOpen() {
    val matcher = SyntaxChecker.POEM_OPEN.matcher("<poem>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findPoemClose() {
    val matcher = SyntaxChecker.POEM_CLOSE.matcher("</poem>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCodeOpen() {
    val matcher = SyntaxChecker.CODE_OPEN.matcher("<code>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCodeClose() {
    val matcher = SyntaxChecker.CODE_CLOSE.matcher("</code>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBlockquoteOpen() {
    val matcher = SyntaxChecker.BLOCKQUOTE_OPEN.matcher("<blockquote>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findBlockquotelose() {
    val matcher = SyntaxChecker.BLOCKQUOTE_CLOSE.matcher("</blockquote>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDivOpen() {
    val matcher = SyntaxChecker.DIV_OPEN.matcher("<div>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findDivClose() {
    val matcher = SyntaxChecker.DIV_CLOSE.matcher("</div>");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findStackTextOpen() {
    val matcher = SyntaxChecker.STACK_TEXT_OPEN.matcher("{{stack");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findStackTextClose() {
    val matcher = SyntaxChecker.STACK_TEXT_CLOSE.matcher("}}");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCommentOpen() {
    val matcher = SyntaxChecker.COMMENT_OPEN.matcher("<!--");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findCommentClose() {
    val matcher = SyntaxChecker.COMMENT_CLOSE.matcher("-->");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findTOC() {
    val matcher = SyntaxChecker.TABLE_OF_CONTENTS.matcher("__TOC__");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void findHeading() {
    val matcher = SyntaxChecker.HEADING.matcher("==");
    assertThat(matcher.find()).isTrue();
  }

  @Test
  void testCheckOpenAndCloseSyntaxCount() {
    val clipCount = SyntaxChecker.openAndCloseSyntaxRatio(testContent);
    assertThat(clipCount).isEqualTo(1);
  }

}

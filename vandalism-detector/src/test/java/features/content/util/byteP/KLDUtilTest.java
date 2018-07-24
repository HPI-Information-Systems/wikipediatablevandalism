package features.content.util.byteP;

import static org.assertj.core.api.Assertions.assertThat;

import features.content.util.byteP.KLDUtil;
import lombok.val;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KLDUtilTest {

  private String basicString;
  private String addSensefullString;
  private String addNonsenseString;
  private String deletedString;

  @BeforeEach
  void setup() {
    basicString = "This wharf comprises a single long pier that ends in a pontoon with two docking " +
        "stations. However, only one of them is used at a time due to size constraints. " +
        "This terminal has a covered passenger waiting area with a Telstra pay phone and a " +
        "drinking water fountain. It takes its name from the adjacent Guyatt Park. " +
        "The wharf sustained moderate damage during the January 2011 Brisbane floods.[3] " +
        "It reopened after repairs on 14 February 2011.[4][5]";

    addSensefullString = basicString + basicString.substring(basicString.length()/2);

    addNonsenseString = basicString + " wwww +ä# $%& $&%$ wwww  ww 654441212465465621532130         .......,,,";

    deletedString = basicString.substring(basicString.length()/2);
  }

  @Test
  void KLDIdentic() {
    val divergence = KLDUtil.calculateKLDOfAddedChars(basicString, basicString);
    assertThat(divergence).isEqualTo(0.0);
  }

  @Test
  void KLDAddSensefull() {
    val divergence = KLDUtil.calculateKLDOfAddedChars(basicString, addSensefullString);
    assertThat(divergence).isCloseTo(0.1, Offset.offset(0.1));
  }

  @Test
  void KLDAddNonsense() {
    val divergence = KLDUtil.calculateKLDOfAddedChars(basicString, addNonsenseString);
    assertThat(divergence).isCloseTo(200.0, Offset.offset(5.0));
  }

  @Test
  void KLDDeleted() {
    val divergence = KLDUtil.calculateKLDOfAddedChars(basicString, deletedString);
    assertThat(divergence).isCloseTo(0.5, Offset.offset(0.5));
  }

}

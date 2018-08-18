package features.content.util.byteP;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class KLDTest {

  private String basicString;
  private String addMeaningfulString;
  private String addNonsenseString;
  private String deletedString;

  @BeforeEach
  void setup() {
    basicString =
        "This wharf comprises a single long pier that ends in a pontoon with two docking " +
            "stations. However, only one of them is used at a time due to size constraints. " +
            "This terminal has a covered passenger waiting area with a Telstra pay phone and a " +
            "drinking water fountain. It takes its name from the adjacent Guyatt Park. " +
            "The wharf sustained moderate damage during the January 2011 Brisbane floods.[3] " +
            "It reopened after repairs on 14 February 2011.[4][5]";

    addMeaningfulString = basicString + basicString.substring(basicString.length() / 2);

    addNonsenseString =
        basicString + " wwww +ä# $%& $&%$ wwww  ww 654441212465465621532130         .......,,,";

    deletedString = basicString.substring(basicString.length() / 2);
  }

  @Nested
  class AddedCharKLD {

    @Test
    void KLDIdentical() {
      val divergence = KLD.calculateKLDOfAddedChars(basicString, basicString);
      assertThat(divergence).isEqualTo(0.0);
    }

    @Test
    void KLDAddMeaningful() {
      val divergence = KLD.calculateKLDOfAddedChars(basicString, addMeaningfulString);
      assertThat(divergence).isCloseTo(31, Offset.offset(1.0));
    }

    @Test
    void KLDAddNonsense() {
      val divergence = KLD.calculateKLDOfAddedChars(basicString, addNonsenseString);
      assertThat(divergence).isCloseTo(152.0, Offset.offset(1.0));
    }

    @Test
    void KLDDeleted() {
      val divergence = KLD.calculateKLDOfAddedChars(basicString, deletedString);
      assertThat(divergence).isZero();
    }
  }

  @Nested
  class CharKLD {

    @Test
    void KLDIdentical() {
      val divergence = KLD.calculateKLD(basicString, basicString);
      assertThat(divergence).isEqualTo(0.0);
    }

    @Test
    void KLDAddMeaningful() {
      val divergence = KLD.calculateKLD(basicString, addMeaningfulString);
      assertThat(divergence).isCloseTo(7, Offset.offset(1.0));
    }

    @Test
    void KLDAddNonsense() {
      val divergence = KLD.calculateKLD(basicString, addNonsenseString);
      assertThat(divergence).isCloseTo(74.0, Offset.offset(1.0));
    }

    @Test
    void KLDDeleted() {
      val divergence = KLD.calculateKLD(basicString, deletedString);
      assertThat(divergence).isCloseTo(31.0, Offset.offset(1.0));
    }
  }
}

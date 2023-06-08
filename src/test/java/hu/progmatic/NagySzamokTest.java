package hu.progmatic;

import hu.progmatic.helper.BadgeExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BadgeExtension.class)
class NagySzamokTest {

  @Test
  @DisplayName("Kis számok 5p")
  void kisSzamokOsszeadas() {
    assertEquals("10", NagySzamok.osszeadas("5", "5"));
    assertEquals("82", NagySzamok.osszeadas("54", "28"));
    assertEquals("7706", NagySzamok.osszeadas("7478", "228"));
    assertEquals("500000", NagySzamok.osszeadas("100001", "399999"));
  }

  @Test
  @DisplayName("Int max 5p")
  void intMaxOsszead() {
    assertEquals("4294967294", NagySzamok.osszeadas("2147483647", "2147483647"));
  }

  @Test
  @DisplayName("Long max 10p")
  void longMaxOsszead() {
    assertEquals("18446744073709551614", NagySzamok.osszeadas("9223372036854775807", "9223372036854775807"));
  }

  @Test
  @DisplayName("Kis számok 5p")
  void kisSzamokSzorzasa() {
    assertEquals("0", NagySzamok.szorzas("95", "0"));
    assertEquals("0", NagySzamok.szorzas("0", "75"));
    assertEquals("1", NagySzamok.szorzas("1", "1"));
    assertEquals("2", NagySzamok.szorzas("1", "2"));
    assertEquals("372", NagySzamok.szorzas("31", "12"));
    assertEquals("75552", NagySzamok.szorzas("787", "96"));
    assertEquals("41751613362", NagySzamok.szorzas("913782", "45691"));
  }

  @Test
  @DisplayName("Int max 5p")
  void intMaxSzorzas() {
    assertEquals("4611686014132420609", NagySzamok.szorzas("2147483647","2147483647"));
  }

  @Test
  @DisplayName("Long max 10p")
  void longMaxValue() {
    assertEquals("85070591730234615847396907784232501249", NagySzamok.szorzas("9223372036854775807","9223372036854775807"));
  }

  @Test
  @DisplayName("Egyszerű faktoriális 5p")
  void egyszeruFaktorialisok() {
    assertEquals("1", NagySzamok.faktorialis("0"));
    assertEquals("2", NagySzamok.faktorialis("2"));
    assertEquals("120", NagySzamok.faktorialis("5"));
    assertEquals("3628800", NagySzamok.faktorialis("10"));
    assertEquals("6402373705728000", NagySzamok.faktorialis("18"));
  }

  @Test
  @DisplayName("Száz faktoriális 10p")
  void szazFaktorialis() {
    assertEquals("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000",
        NagySzamok.faktorialis("100"));
  }
}
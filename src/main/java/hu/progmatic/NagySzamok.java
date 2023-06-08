package hu.progmatic;

import java.math.BigInteger;

public class NagySzamok {

  public static String osszeadas(String szam1, String szam2) {
    BigInteger elsoSzam = new BigInteger(szam1);
    BigInteger masodikSzam = new BigInteger(szam2);
    BigInteger sum = elsoSzam.add(masodikSzam);

    return String.valueOf(sum);
  }

  public static String szorzas(String szam1, String szam2) {
    BigInteger elsoSzam = new BigInteger(szam1);
    BigInteger masodikSzam = new BigInteger(szam2);
    BigInteger sum = elsoSzam.multiply(masodikSzam);
    return String.valueOf(sum);
  }

  /*public static String faktorialis(String szam) {
    int szamInt = Integer.parseInt(szam);
    BigInteger eredmeny = new BigInteger(szam);
    if (szamInt < 2) {
      return "1";
    }
    for (int i = 2; i < szamInt; i++) {
      eredmeny = eredmeny.multiply(BigInteger.valueOf(i));
    }
    return String.valueOf(eredmeny);
  }*/

  public static String faktorialis(String szam) {
    int szamInt = Integer.parseInt(szam);
    BigInteger eredmeny = new BigInteger(szam);
    if (szamInt < 2) {
      return "1";
    }
    return String.valueOf(eredmeny.multiply(new BigInteger(faktorialis(String.valueOf(szamInt - 1)))));
  }

}

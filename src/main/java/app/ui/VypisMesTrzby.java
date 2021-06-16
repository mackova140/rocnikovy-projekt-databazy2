package app.ui;

import app.rdg.MesTrzba;

/**
 *
 * @author Natalia
 */

public class VypisMesTrzby {

    private static final VypisMesTrzby INSTANCE = new VypisMesTrzby();

    public static VypisMesTrzby getInstance() {
        return INSTANCE;
    }

    private VypisMesTrzby() { }

    public void print(MesTrzba mesTrzba) {
        if (mesTrzba == null) {
            throw new NullPointerException("Ucet nesmie byt null");
        }

        System.out.print("Id podniku:      ");
        System.out.println(mesTrzba.getId_podnik());
        System.out.print("Za obdobie:      ");
        System.out.println(mesTrzba.getYear_month());
        System.out.print("Pocet predaných:  ");
        System.out.println(mesTrzba.getPocet());
        System.out.print("Trzba:           ");
        System.out.println(mesTrzba.getCena());
        System.out.println();
    }
}
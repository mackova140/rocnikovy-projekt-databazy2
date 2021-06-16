package app.ui;

import app.rdg.StatistikaNapojMesiac;

public class VypisStatistikaNM {
    private static final VypisStatistikaNM INSTANCE = new VypisStatistikaNM();

    public static VypisStatistikaNM getInstance() {
        return INSTANCE;
    }

    private VypisStatistikaNM() { }

    public void print(StatistikaNapojMesiac s) {
        if (s == null) {
            throw new NullPointerException("nesmie byt null");
        }

        System.out.print("Id podniku a napoja: ");
        System.out.println(s.getPodnik_napoj());
        System.out.print("Za obdobie:          ");
        System.out.println(s.getDatum());
        System.out.print("Pocet predaných:     ");
        System.out.println(s.getPocet());
        System.out.println();
    }
}

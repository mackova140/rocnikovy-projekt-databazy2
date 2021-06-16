package app.ui;

import app.rdg.Napoj;

/**
 *  @author Natalia Mackova
 */

public class VypisMenu {
    private static final VypisMenu INSTANCE = new VypisMenu();

    public static VypisMenu getInstance() { return INSTANCE; }

    private VypisMenu() { }

    public void print(Napoj napoj) {
        if (napoj == null) { throw new NullPointerException("Napoj nesmie byt null"); }

        System.out.print("Id podniku: ");
        System.out.println(napoj.getId_podnik());
        System.out.print("Id nápoja:  ");
        System.out.println(napoj.getId());
        System.out.print("Názov:      ");
        System.out.println(napoj.getNazov());
        System.out.print("Objem:      ");
        System.out.println(napoj.getObjem());
        System.out.print("Cena:       ");
        System.out.println(napoj.getPredajna_cena());
        System.out.print("Skladové zásoby:  ");
        System.out.println(napoj.getMnozstvo());
        System.out.println();
    }

    public void printSkladoveZasoby(Napoj napoj) {
        if (napoj == null) { throw new NullPointerException("Napoj nesmie byt null"); }

        System.out.print("Id podniku:       ");
        System.out.println(napoj.getId_podnik());
        System.out.print("Id nápoja:        ");
        System.out.println(napoj.getId());
        System.out.print("Názov:            ");
        System.out.println(napoj.getNazov());
        System.out.print("Skladové zásoby:  ");
        System.out.println(napoj.getMnozstvo());
        System.out.println();
    }
}

package app.ui;

import app.rdg.Zakaznik;

/**
 *  @author Natalia Mackova
 */

public class VypisZakaznika {
    private static final VypisZakaznika INSTANCE = new VypisZakaznika();

    public static VypisZakaznika getInstance() { return INSTANCE; }

    private VypisZakaznika() { }

    public void print(Zakaznik zakaznik) {
        if (zakaznik == null) {
            throw new NullPointerException("Zakaznik nesmie byt null");
        }

        System.out.print("Id :             ");
        System.out.println(zakaznik.getId());
        System.out.print("Meno:            ");
        System.out.println(zakaznik.getMeno());
        System.out.print("Heslo:           ");
        System.out.println(zakaznik.getHeslo());
        System.out.print("Aktuálny kredit: ");
        System.out.println(zakaznik.getKredit());
        System.out.println();
    }
}

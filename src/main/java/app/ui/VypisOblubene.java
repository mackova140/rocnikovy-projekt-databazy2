package app.ui;

import app.rdg.Oblubene;

import java.sql.SQLException;

public class VypisOblubene {
    private static final VypisOblubene INSTANCE = new VypisOblubene();

    public static VypisOblubene getInstance() { return INSTANCE; }

    private VypisOblubene() { }

    public void print(Oblubene o) throws SQLException {
        if (o == null) { throw new NullPointerException("Id nesmie byt null"); }

        System.out.print("Id nápoja:        ");
        System.out.println(o.getId_napoj());
        System.out.print("Názov:            ");
        System.out.println(o.getNazov());
        System.out.print("Počet objednávok: ");
        System.out.println(o.getPocet());
        System.out.println();
    }
}

package app.ui;

import app.rdg.NajdiNapoj;
import app.rdg.Objednavky;

import java.sql.SQLException;

public class VypisNapojeStol {
    private static final VypisNapojeStol INSTANCE = new VypisNapojeStol();

    public static VypisNapojeStol getInstance() { return INSTANCE; }

    private VypisNapojeStol() { }

    public void print(Objednavky o) throws SQLException {
        if (o == null) { throw new NullPointerException("Objedávka nesmie byt null"); }

        System.out.print("Id objednavky:  ");
        System.out.println(o.getId());
        System.out.print("Id nápoja:  ");
        System.out.println(o.getId_napoj());
        System.out.print("Názov:      ");
        System.out.println(NajdiNapoj.getInstance().findById(o.getId_napoj()).getNazov());

    }
}

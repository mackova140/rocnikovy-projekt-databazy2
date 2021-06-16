package app.ui;

import app.rdg.Ucet;

import java.sql.SQLException;

public class VypisZNP {
    private static final VypisZNP INSTANCE = new VypisZNP();

    public static VypisZNP getInstance() { return INSTANCE; }

    private VypisZNP() { }

    public void print(Ucet o) throws SQLException {
        if (o == null) { throw new NullPointerException("Id nesmie byt null"); }

        System.out.print("Id zákazníka:   ");
        System.out.println(o.getId_zakaznik());
        System.out.print("Id nápoja:      ");
        System.out.println(o.getId_napoj());
    }
}

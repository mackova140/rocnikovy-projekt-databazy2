package app.ui;

import app.rdg.Podnik;

import java.sql.SQLException;

public class VypisNavstevy {
    private static final VypisNavstevy INSTANCE = new VypisNavstevy();

    public static VypisNavstevy getInstance() { return INSTANCE; }

    private VypisNavstevy() { }

    public void print(Podnik o) throws SQLException {
        if (o == null) { throw new NullPointerException("Id nesmie byt null"); }


        System.out.print("Id podniku " + o.getId() +  "    Názov: ");
        System.out.println(o.getNazov());
    }
}

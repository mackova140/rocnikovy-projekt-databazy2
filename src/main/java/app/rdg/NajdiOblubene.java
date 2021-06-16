package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NajdiOblubene {
    private static final NajdiOblubene INSTANCE = new NajdiOblubene();

    public static NajdiOblubene getInstance() {
        return INSTANCE;
    }

    private NajdiOblubene() {}


    public List<Oblubene> findTop3(int id_zakaznik) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("select * from oblubene \n" +
                "WHERE id_zakaznik = ? \n" +
                "order by pocet desc LIMIT 3")) {

            s.setInt(1, id_zakaznik);

            try (ResultSet r = s.executeQuery()) {
                List<Oblubene> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }
                return elements;
            }
        }
    }


    protected Oblubene load(ResultSet r) throws SQLException {
        Oblubene c = new Oblubene();

        c.setId(r.getInt("id"));
        c.setId_napoj(r.getInt("id_napoj"));
        c.setId_zakaznik(r.getInt("id_zakaznik"));
        c.setNazov(r.getString("nazov"));
        c.setPocet(r.getInt("pocet"));
        return c;
    }
}

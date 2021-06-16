package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NajdiUcet {
    private static final NajdiUcet INSTANCE = new NajdiUcet();

    public static NajdiUcet getInstance() { return INSTANCE; }

    private NajdiUcet() { }

    public List<Ucet> statistikaZNP() throws SQLException {    // zakaznik napoj podmik

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                    "select u.id_zakaznik, u.id_napoj from ucet u \n" +
                        "where u.id_podnik in (Select n.id_podnik from napojovy_listok n where n.id_napoj = u.id_napoj) \n" +
                        "group by u.id_zakaznik, u.id_napoj \n" +
                        "having count(distinct u.id_podnik) = (Select  count(a.id_podnik) from napojovy_listok a where a.id_napoj = u.id_napoj )")) {

            try (ResultSet r = s.executeQuery()) {
                List<Ucet> elements = new ArrayList<>();

                while (r.next()) {
                    Ucet c = new Ucet();
                    c.setId_zakaznik(r.getInt("id_zakaznik"));
                    c.setId_napoj(r.getInt("id_napoj"));

                    elements.add(c);
                }
                return elements;

            }
        }
    }

    protected Ucet load(ResultSet r) throws SQLException {
        Ucet c = new Ucet();
        c.setId(r.getInt("id"));
        c.setId_zakaznik(r.getInt("id_zakaznik"));
        c.setId_podnik(r.getInt("id_podnik"));
        c.setId_napoj(r.getInt("id_napoj"));
        c.setDatum(r.getDate("datum"));
        c.setCena(r.getDouble("cena"));

        return c;
    }
}

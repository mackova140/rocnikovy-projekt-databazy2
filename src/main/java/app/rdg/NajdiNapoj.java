package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author Natalia Mackova
 */

public class NajdiNapoj {
    private static final NajdiNapoj INSTANCE = new NajdiNapoj();

    public static NajdiNapoj getInstance() {
        return INSTANCE;
    }

    private NajdiNapoj() {}

    public Napoj findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM napojovy_listok WHERE id = ?")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Napoj c = load(r);

                    if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }

                    return c;

                } else return null;
            }
        }
    }

    public List<Napoj> findByIdSklad(int id, int podnik) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM napojovy_listok WHERE id_napoj = ? and id_podnik = ? ")) {
            s.setInt(1, id);
            s.setInt(2, podnik);

            try (ResultSet r = s.executeQuery()) {
                List<Napoj> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }
                return elements;
            }
        }
    }

    public List<Napoj> findAllByPodnik(Integer value) throws SQLException {
        if (value == null) {
            throw new NullPointerException("Id nemoze byt null.");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("select * from napojovy_listok where id_podnik = ?")) {
            s.setInt(1, value);

            try (ResultSet r = s.executeQuery()) {
                List<Napoj> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }
                return elements;

            }
        }
    }

    protected Napoj load(ResultSet r) throws SQLException {
        Napoj c = new Napoj();

        c.setId(r.getInt("id"));
        c.setId_podnik(r.getInt("id_podnik"));
        c.setId_napoj(r.getInt("id_napoj"));
        c.setNazov(r.getString("nazov"));
        c.setPredajna_cena(r.getDouble("predajna_cena"));
        c.setObjem(r.getDouble("objem"));
        c.setMnozstvo(r.getInt("mnozstvo"));
        return c;
    }
}

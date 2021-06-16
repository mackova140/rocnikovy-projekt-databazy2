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

public class NajdiStol {
    private static final NajdiStol INSTANCE = new NajdiStol();

    public static NajdiStol getInstance() {
        return INSTANCE;
    }

    private NajdiStol() {}

    public Stol findById(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM stoly WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Stol c = new Stol();
                    c.setPocet(r.getInt("pocet_miest"));
                    c.setId(r.getInt("id"));
                    c.setId_podnik(r.getInt("id_podnik"));

                    if (r.next()) {
                        throw new RuntimeException("Neocakavana chyba");
                    }
                    return c;

                } else return null;

            }
        }
    }

    public List<Stol> findByPodnik_id(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM stoly WHERE id_podnik = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                List<Stol> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }

                return elements;
            }
        }
    }

    protected Stol load(ResultSet r) throws SQLException {
        Stol c = new Stol();

        c.setId(r.getInt("id"));
        c.setId_podnik(r.getInt("id_podnik"));
        c.setPocet(r.getInt("pocet_miest"));

        if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }
        return c;
    }
}

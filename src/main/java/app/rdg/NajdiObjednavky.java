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

public class NajdiObjednavky {
    private static final NajdiObjednavky INSTANCE = new NajdiObjednavky();

    public static NajdiObjednavky getInstance() { return INSTANCE; }

    private NajdiObjednavky() { }

    public Objednavky findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM objednavky WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Objednavky c = new Objednavky();

                    c.setId(r.getInt("id"));
                    c.setId_stol(r.getInt("id_stol"));
                    c.setId_napoj(r.getInt("id_napoj"));

                    if (r.next()) {
                        throw new RuntimeException("Neocakavana chyba");
                    }
                    return c;

                } else return null;

            }
        }
    }

    public List<Objednavky> findAllByStol(Integer value) throws SQLException {
        if (value == null) {
            throw new NullPointerException("Id nemoze byt null.");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("select * from objednavky where id_stol = ?")) {
            s.setInt(1, value);

            try (ResultSet r = s.executeQuery()) {
                List<Objednavky> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }
                return elements;

            }
        }
    }

    protected Objednavky load(ResultSet r) throws SQLException {
        Objednavky c = new Objednavky();

        c.setId(r.getInt("id"));
        c.setId_stol(r.getInt("id_stol"));
        c.setId_napoj(r.getInt("id_napoj"));

        return c;
    }
}

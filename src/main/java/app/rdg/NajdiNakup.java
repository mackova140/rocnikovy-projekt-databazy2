package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// todo treba????
public class NajdiNakup {
    private static final NajdiNakup INSTANCE = new NajdiNakup();

    public static NajdiNakup getInstance() {
        return INSTANCE;
    }

    private NajdiNakup() {}

    public Nakup findById(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM nakupy WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Nakup c = load(r);

                    if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }

                    return c;

                } else return null;
            }
        }
    }


    protected Nakup load(ResultSet r) throws SQLException {
        Nakup c = new Nakup();

        c.setId(r.getInt("id"));
        c.setDatum(r.getDate("datum"));
        c.setSuma(r.getDouble("suma"));
        return c;
    }
}

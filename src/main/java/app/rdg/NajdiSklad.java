package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NajdiSklad {
    private static final NajdiSklad INSTANCE = new NajdiSklad();

    public static NajdiSklad getInstance() {
        return INSTANCE;
    }

    private NajdiSklad() {}

    public Sklad findById(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM velkosklad WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Sklad c = load(r);

                    if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }

                    return c;

                } else return null;
            }
        }
    }



    protected Sklad load(ResultSet r) throws SQLException {
        Sklad c = new Sklad();

        c.setId(r.getInt("id"));
        c.setNazov(r.getString("nazov"));
        c.setNakupnaCena(r.getDouble("nakupna_cena"));
        c.setObjem(r.getDouble("objem"));
        return c;
    }
}

package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NajdiPodnik {
    private static final NajdiPodnik INSTANCE = new NajdiPodnik();

    public static NajdiPodnik getInstance() {
        return INSTANCE;
    }

    private NajdiPodnik() {}

    public Podnik findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM podnik WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Podnik c = load(r);

                    if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }

                    return c;

                } else return null;
            }
        }
    }

    public List<Integer> findNAvstevy(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "select id_podnik, count(*) as pocet from ucet \n" +
                " where id_zakaznik = ? \n" +
                " group by id_podnik")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                List<Integer> p = new ArrayList<>();

                while (r.next()) {
                    p.add(r.getInt("id_podnik"));
                }

                return p;
            }
        }
    }



    protected Podnik load(ResultSet r) throws SQLException {
        Podnik c = new Podnik();

        c.setId(r.getInt("id"));
        c.setNazov(r.getString("nazov"));
        return c;
    }
}

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

public class NajdiMesTrzba {
    private static final NajdiMesTrzba INSTANCE = new NajdiMesTrzba();

    public static NajdiMesTrzba getInstance() {
        return INSTANCE;
    }

    private NajdiMesTrzba() { }

    public List<MesTrzba> mesacnaTrzba(String mr) throws SQLException {         // calendar
        if (mr == null) {
            throw new NullPointerException("Musíš zadať rok a mesiac");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "select distinct id_podnik as id_podnik, \n" +
                "                 to_char(datum, 'YYYY-MM') as year_month, \n" +
                "                 sum(cena) as cena, \n" +
                "                 count(id) as pocet  \n" +
                "from ucet \n" +
                "where to_char(datum, 'YYYY-MM') = ? \n" +
                "group by id_podnik, year_month\n" +
                "order by id_podnik, year_month")) {

            s.setString(1, mr);
            try (ResultSet r = s.executeQuery()) {
                List<MesTrzba> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }

                return elements;
            }
        }
    }

    protected MesTrzba load(ResultSet r) throws SQLException {
        MesTrzba c = new MesTrzba();
        c.setId_podnik(r.getInt("id_podnik"));
        c.setYear_month(r.getString("year_month"));
        c.setCena(r.getDouble("cena"));
        c.setPocet(r.getInt("pocet"));
        return c;
    }
}

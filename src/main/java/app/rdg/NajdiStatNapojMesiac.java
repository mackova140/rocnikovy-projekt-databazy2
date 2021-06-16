package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NajdiStatNapojMesiac {

    private static final NajdiStatNapojMesiac INSTANCE = new NajdiStatNapojMesiac();

    public static NajdiStatNapojMesiac getInstance() {
        return INSTANCE;
    }

    private NajdiStatNapojMesiac() {}

    public List<StatistikaNapojMesiac> statistikaNapojMesiaca() throws SQLException {    // zakaznik napoj podmik

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "select distinct(id_podnik, id_napoj) as podnik_napoj, to_char(datum, 'YYYY-MM') as datum, count(*) as pocet from ucet\n" +
                        "where to_char(datum, 'YYYY-MM') > '2020-11'\n" +
                        "group by id_podnik, id_napoj, to_char(datum, 'YYYY-MM')\n" +
                        "order by count(*) desc LIMIT 100")) {

            try (ResultSet r = s.executeQuery()) {
                List<StatistikaNapojMesiac> elements = new ArrayList<>();

                while (r.next()) {
                    StatistikaNapojMesiac c = new StatistikaNapojMesiac();
                    c.setPodnik_napoj(r.getString("podnik_napoj"));
                    c.setDatum(r.getString("datum"));
                    c.setPocet(r.getInt("pocet"));

                    elements.add(c);
                }
                return elements;
            }
        }
    }

    protected StatistikaNapojMesiac load(ResultSet r) throws SQLException {
        StatistikaNapojMesiac c = new StatistikaNapojMesiac();

        c.setPodnik_napoj(r.getString("podnik_napoj"));
        c.setDatum(r.getString("datum"));
        c.setPocet(r.getInt("pocet"));
        return c;
    }
}

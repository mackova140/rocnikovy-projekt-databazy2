package app.ts;

import app.DbContext;
import app.rdg.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VykonajObjednavka {
    private static final VykonajObjednavka INSTANCE = new VykonajObjednavka();

    public static VykonajObjednavka getInstance() { return INSTANCE; }

    public List<Ucet> vytvoreneO(int id_zakaznik, int id_stol, List<Objednavky> o) throws ObjednavkaException, SQLException {

        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        DbContext.getConnection().setAutoCommit(false);

        // potrebujeme vysoku ochranu

        try {
            Zakaznik zak = NajdiZakazika.getInstance().findById(id_zakaznik);
            // kontrola zakaznika
            if (zak == null) { throw new ObjednavkaException("Vaše id nie je správne."); }
            Karta karta = NajdiKarta.getInstance().findByZakaznik(id_zakaznik);
            double zlava = karta.getId() == null? 0 : karta.getZlava_percenta();

            Stol stol = NajdiStol.getInstance().findById(id_stol);
            // kontrola stolu
            if (stol == null) { throw new ObjednavkaException("Taky stol neexistuje"); }
            int id_podnik = stol.getId_podnik();

            java.util.Date uDate = new java.util.Date(); java.sql.Date sDate = new java.sql.Date(uDate.getTime());

            List<Ucet> platba = new ArrayList<Ucet>();
            for (Objednavky onj : o){
                Ucet u = new Ucet();

                double poZlave = NajdiNapoj.getInstance().findById(onj.getId_napoj()).getPredajna_cena();
                poZlave -= poZlave*zlava;

                u.setCena(poZlave);
                u.setDatum(sDate);
                u.setId_podnik(id_podnik);
                u.setId_napoj(onj.getId_napoj());
                u.setId_zakaznik(id_zakaznik);

                platba.add(u);
                // u.insert();
            }

            DbContext.getConnection().commit();
            return platba;
        } catch(SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        }
        finally{
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}

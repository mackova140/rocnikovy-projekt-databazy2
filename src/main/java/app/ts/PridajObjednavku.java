package app.ts;

import app.DbContext;
import app.rdg.*;

import java.sql.Connection;
import java.sql.SQLException;


public class PridajObjednavku {
    private static final PridajObjednavku INSTANCE = new PridajObjednavku();

    public static PridajObjednavku getInstance() { return INSTANCE; }

    public boolean vloz(int id_stol, int id_napoj) throws ObjednavkaException, SQLException {
        if (id_napoj <= 0 || id_stol <= 0) {
            throw new IllegalArgumentException("Id nesmie byt mensie ako 1.");
        }

        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);  // pre update mnozstva musi vediet ci nebolo menene
        DbContext.getConnection().setAutoCommit(false);

        try {
            Stol stol = NajdiStol.getInstance().findById(id_stol);

    // kontrola stolu
            if (stol == null) { throw new ObjednavkaException("Taky stol neexistuje"); }
            int ide_podnikS = stol.getId_podnik();

    // kontrola napoja
            Napoj napoj = NajdiNapoj.getInstance().findById(id_napoj);
            if (napoj == null) { throw new ObjednavkaException("Taky napoj neexistuje"); }
            int ide_podnikN = napoj.getId_podnik();

            if (ide_podnikN != ide_podnikS) throw new ObjednavkaException("Tento nápaj nepatrí podniku s id "
                                        + ide_podnikS + ". Prečítajte si prosím znova nápojový lístok.");

    // kontrola množstva napoja na sklade
            int mn = napoj.getMnozstvo();
            if (mn < 1) { throw new IllegalArgumentException("Na sklade podniku nie je dostatok napoja."); }

            napoj.setMnozstvo(mn - 1);  //mn--
            napoj.update();

    // kontrola vytvorenia objednavky
            Objednavky p = new Objednavky();
            p.setId_stol(id_stol);
            p.setId_napoj(id_napoj);
            p.insert();

            Integer pid = p.getId();
            Objednavky o = NajdiObjednavky.getInstance().findById(pid);

            if (o == null) { throw new ObjednavkaException("Táto objedávka sa nevykonala"); }

            DbContext.getConnection().commit();

            return true;

        } catch(SQLException | ObjednavkaException e) {
            DbContext.getConnection().rollback();
            throw e;
        }
        finally{
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}

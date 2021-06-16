package app.ts;

import app.DbContext;
import app.rdg.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PotvrdObjednavka {
    private static final PotvrdObjednavka INSTANCE = new PotvrdObjednavka();

    public static PotvrdObjednavka getInstance() { return INSTANCE; }

    public boolean zaplat(Integer zakaznik, double suma, List<Ucet> u, List<Objednavky> o) throws ObjednavkaException, SQLException {

        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        DbContext.getConnection().setAutoCommit(false);
        try {

// vysoka ochrana

            Zakaznik zak = NajdiZakazika.getInstance().findById(zakaznik);
            // kontrola zakaznika
            if (zak == null) { throw new ObjednavkaException("Vaše id nie je správne."); }
            if (zak.getKredit() - suma < 0){ throw new ObjednavkaException("Nemáš dostatočný kredit"); }
            if (u.size() != o.size()){ throw new ObjednavkaException("Nejaká objednávka bola už zaplatená."); }

            zak.setKredit(zak.getKredit() - suma);
            zak.update();

            Prevadzkovatel p = NajdiPrevadz.getInstance().find();
            p.setKredit(p.getKredit() + suma);
            p.update();

            for (int i = 0; i < u.size(); i++){
                if (NajdiObjednavky.getInstance().findById(o.get(i).getId()) == null )
                    throw new ObjednavkaException("Objednávka ktorú chcete zaplatit uz neexistuje.");

                 u.get(i).insert();
                 o.get(i).delete();
            }

            DbContext.getConnection().commit();
            return true;
        } catch(SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        }
        finally{
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}

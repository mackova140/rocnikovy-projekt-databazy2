package app.ts;

import app.DbContext;
import app.rdg.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

// objednavka PODNIKU pre doplnenie skladových zásob
public class ObjednavkaSklad {
    private static final ObjednavkaSklad INSTANCE = new ObjednavkaSklad();

    public static ObjednavkaSklad getInstance() { return INSTANCE; }

    public int objednaj(int id_podnik, int id_napoj, int mn, double zarobok) throws ObjednavkaException, SQLException, ParseException {

        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        // nechceme aby sa nam menili udaje, treba vediet či drink uz existuje == zbytočne duplicity
        // aby niekto nenakupil tolko ze by sme nemali peniaze pri vykonavani tejto objednavky
        // ked nastane zmena chceme o tom vediet

        DbContext.getConnection().setAutoCommit(false);

        try {
            Sklad napoj = NajdiSklad.getInstance().findById(id_napoj);
            Podnik podnik = NajdiPodnik.getInstance().findById(id_podnik);
            Prevadzkovatel p = NajdiPrevadz.getInstance().find();

            if (napoj == null) {
                throw new ObjednavkaException("Taky napoj nie je v databáze.");
            }

            if (podnik == null){
                throw new ObjednavkaException("Daný podnik neexistuje.");
            }

            if (p == null){
                throw new ObjednavkaException("Chyba na strane prevádzkovateľa.");
            }

            // celková suma nákupu
            double suma = napoj.getNakupna_cena() * mn;
            double kredit = p.getKredit();
            int vysledok;

            if (kredit-suma < 0){
                throw new ObjednavkaException("Nemožete objednať nápoj. Máte nízky kredit.");
            }

            List<Napoj> n = NajdiNapoj.getInstance().findByIdSklad(id_napoj, id_podnik);

            // nenaslo napoj
            if (n == null || n.isEmpty()){
                Napoj pridaj = new Napoj();

                pridaj.setId_podnik(id_podnik);
                pridaj.setId_napoj(id_napoj);
                pridaj.setNazov(napoj.getNazov());
                pridaj.setPredajna_cena(napoj.getNakupna_cena() * zarobok);
                pridaj.setObjem(napoj.getObjem());
                pridaj.setMnozstvo(mn);

                pridaj.insert();

                int idecko = pridaj.getId();
                if (NajdiNapoj.getInstance().findById(idecko) == null){ throw new ObjednavkaException("Nepodarilo sa pridat napoj do Menu."); }
                vysledok = pridaj.getId();
            }
            // naslo updajtne pocet
            else {
                n.get(0).setMnozstvo(n.get(0).getMnozstvo() + mn);
                n.get(0).update();

                if (NajdiNapoj.getInstance().findById(n.get(0).getId()) == null){ throw new ObjednavkaException("Nepodarilo sa pridat napoj do Menu."); }
                vysledok = n.get(0).getId();
            }

            p.setKredit(p.getKredit() - suma);
            p.update();

            // vytvorená "objednavka" sa ukáže v tabulke nákupy
            Nakup nakup = new Nakup();

            java.util.Date uDate = new java.util.Date(); java.sql.Date sDate = new java.sql.Date(uDate.getTime());
            nakup.setDatum(sDate);
            nakup.setSuma(suma);
            nakup.insert();

            DbContext.getConnection().commit();

            return vysledok;

        } catch(Exception e) {
            DbContext.getConnection().rollback();
            throw e;
        }
        finally{
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}

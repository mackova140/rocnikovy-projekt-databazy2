package app.ui;

import app.rdg.*;
import app.ts.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainMenu extends Menu {
    @Override
    public void print() {
        System.out.println("************************************");
        System.out.println("* 1. Vytvor si konto               *");     // funguje
        System.out.println("* 2. Aktualizuj údaje              *");     // funguje
        System.out.println("* 3. Zoznam navštívených podnikov  *");     // funguje
        System.out.println("* 4. Obľúbené nápoje               *");     // funguje
        System.out.println("* 5. Aktualizuj cenu nápoja z MENU *");     // funguje
        System.out.println("* 6. Vypíš nápojový lístok         *");     // funguje
        System.out.println("* 7. Odstráň nápoj z Menu          *");     // funguje
        System.out.println("* 8. Nákup napoja                  *");     // ZLOZITEJSIA funguje
        System.out.println("* 9. Vypis skladove zasoby         *");     // funguje
        System.out.println("* 10. Odstran nápoj zo stola       *");     // funguje
        System.out.println("* 11. Výpis nápojov na stole       *");     // funguje
        System.out.println("* 12. Objednaj si nápoj na stol    *");     // ZLOZITEJSIA funguje
        System.out.println("* 13. Vytvor účet a zaplať.        *");     // ZLOZITEJSIA funguje
        System.out.println("* 17. Mesacna trzba                *");     // STATISTIKA funguje
        System.out.println("* 18. Napoje mesiaca               *");     // funguje
        System.out.println("* 19. Zakaznik - Napoj - Podnik    *");     // funguje
        System.out.println("* 20. exit                         *");
        System.out.println("************************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "1":
                    pridajZakaznika();
                    break;
                case "2":
                    upravZakaznika();
                    break;
                case "3":
                    navstivenePodniky();
                    break;
                case "4":
                    vypisOblubene();
                    break;
                case "5":
                    zmenCenu();
                    break;
                case "6":
                    vypisMenu();
                    break;
                case "7":
                    deleteNapoj();
                    break;
                case "8":
                    nakupNapoj();
                    break;
                case "9":
                    vypisZasoby();
                    break;
                case "10":
                    vymazObjednavka();
                    break;
                case "11":
                    vypisNapojeNaStole();
                    break;
                case "12":
                    pridajObjednavku();
                    break;
                case "13":
                    platba();
                    break;
                case "17":
                    mesacnaTrzba();
                    break;
                case "18":
                    napojMesiaca();
                    break;
                case "19":
                    vypisZNP();
                    break;
                case "20":
                    System.out.println("Ďakujeme za návštevu, dovidenia.");
                    exit();
                    break;
                default:
                    System.out.println("Nepovolená operácia.");
                    break;
            }
        } catch (SQLException | IOException | ObjednavkaException e) {
            throw new RuntimeException(e);
        }
    }

    private void pridajZakaznika() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Zakaznik zakaznik = new Zakaznik();

        System.out.println("Zadaj meno:");
        zakaznik.setMeno(br.readLine());
        System.out.println("Zadaj heslo:");
        zakaznik.setHeslo(br.readLine());
        System.out.println("Nastav si kredit:");
        double k = Double.parseDouble(br.readLine());
        zakaznik.setKredit(k);

        zakaznik.insert();

        System.out.println("Konto zákazníka bolo úspešne vytvorené.");
        System.out.print("Bolo Vám pridelené nasledovné ID:");
        System.out.println(zakaznik.getId());
    }

    private void upravZakaznika() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj id zakaznika");
        int customerId = Integer.parseInt(br.readLine());

        Zakaznik zakaznik = NajdiZakazika.getInstance().findById(customerId);

        if (zakaznik == null) {
            System.out.println("Zákazník s daným id neexistuje");
        } else {
            VypisZakaznika.getInstance().print(zakaznik);

            System.out.println("Zadaj meno: ");
            zakaznik.setMeno(br.readLine());
            System.out.println("Zadaj heslo: ");
            zakaznik.setHeslo(br.readLine());
            System.out.println("Nový kredit: ");
            double k = Double.parseDouble(br.readLine());
            zakaznik.setKredit(k);

            zakaznik.update();

            System.out.println("Udaje o zakáznikovi boli úspešne zmenené");
        }
    }

    private void zmenCenu() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj id napoja:");
        int idN = Integer.parseInt(br.readLine());

        Napoj napoj = NajdiNapoj.getInstance().findById(idN);

        if (napoj == null) {
            System.out.println("Nápoj s daným id neexistuje");
        } else {
            System.out.println("Zadaj novu predajnu cenu: ");
            double k = Double.parseDouble(br.readLine());
            napoj.setPredajna_cena(k);
            int n = napoj.getMnozstvo();
            System.out.println(n);
            napoj.update();

            System.out.println("Cena nápoja bola uspešne zmenená.");
        }
    }

    private void deleteNapoj() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj id napoja:");
        int i = Integer.parseInt(br.readLine());

        Napoj napoj = NajdiNapoj.getInstance().findById(i);

        if (napoj == null) {
            System.out.println("Napoj neexistuje");
        } else {
            try {
                napoj.delete();
                System.out.println("Napoj uspesne vymazany.");

            } catch (Exception e){
                System.out.println("Nemozno vymazat napoj ak jeho objednavka nie je spracovana");       // todo ak vymazavam napoj, vymazem ho v cs, on delete ...
            }

        }
    }

    private void vypisMenu() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj id podniku:");
        int p = Integer.parseInt(br.readLine());

        List<Napoj> z = NajdiNapoj.getInstance().findAllByPodnik(p);

        if (z.size() == 0) {
            System.out.println("Tento podnik este nema napojovy listok.");
            return;
        }

        for (Napoj n : z) {
            if (n == null) {
                System.out.println("Taky napoj nie je.");
            } else {
                VypisMenu.getInstance().print(n);
            }
        }
    }

    private void mesacnaTrzba() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj rok a mesiac v tvare 'YYYY-MM':");
        String p = (br.readLine());

        List<MesTrzba> zoz = NajdiMesTrzba.getInstance().mesacnaTrzba(p);

        if (zoz.isEmpty()) {
            System.out.println("Trzba za dany rok a mesiac neexistuje.");
            return;
        }

        for (MesTrzba n : zoz) {
            if (n == null) {
                System.out.println("Nastala chyba v hladaní tržby.");
            } else {
                VypisMesTrzby.getInstance().print(n);
            }
        }
    }

    private void pridajObjednavku() throws IOException, SQLException, ObjednavkaException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // moze si predtym vypisat menu podniku

        System.out.print("Zadaj id stola: ");
        int id_stol = Integer.parseInt(br.readLine());

        System.out.print("Zadaj id napoja z menu: ");
        int id_napoj = Integer.parseInt(br.readLine());

        boolean vysl = false;

        try {
            vysl = PridajObjednavku.getInstance().vloz(id_stol, id_napoj);

        } catch (ObjednavkaException ex) {                  // ak nie je dostatocne mnozstvo tovaru
            System.out.println(ex.getMessage());
        }

        if(vysl){
            System.out.println("Vasa objednavka bola prijata.");
        }
        else{
            System.out.println("Objednavku sa nepodarilo vykonat.");
        }
    }

    private void vypisNapojeNaStole() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ak chceš vypísať objednávky pre stôl, zadaj id stola: ");
        int id_stol = Integer.parseInt(br.readLine());

        List<Objednavky> z = NajdiObjednavky.getInstance().findAllByStol(id_stol);

        if (z.size() == 0) { System.out.println("Tento stolík nemá žiadne objednávky.");
            return;
        }

        for (Objednavky n : z) {
            if (n == null) {
                System.out.println("Taka objedavka nie je.");
            } else {
                VypisNapojeStol.getInstance().print(n);
                //System.out.print(n.getId_napoj() + "");           // todo pre big data odkomentovat
            }
        }
        //System.out.println();
    }

    private void nakupNapoj() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Objednávka pre podnik: ");
        int id_podnik = Integer.parseInt(br.readLine());

        System.out.print("Výrobné id nápoja: ");
        int id_napoj = Integer.parseInt(br.readLine());

        System.out.print("Požadované množstvo? ");
        int mn = Integer.parseInt(br.readLine());

        System.out.print("Predajná cena ak ešte neexistuje: ");
        double c = Double.parseDouble(br.readLine());

        int k = -1;
        try {
            k = ObjednavkaSklad.getInstance().objednaj(id_podnik, id_napoj, mn, c);
        }catch (ObjednavkaException | SQLException | ParseException ex) {                  // ak nie je dostatocne mnozstvo tovaru
            System.out.println(ex.getMessage());
        }

        if (k != -1) {
            System.out.println("Objednávka pre podnik " + id_podnik + " sa vykonala");
            System.out.println("Id kúpeného nápoja je " + k);
        }
        else System.out.println("Zlyhanie.");
    }

    private void vypisZasoby() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj id napoja: ");
        int i = Integer.parseInt(br.readLine());

        Napoj z = NajdiNapoj.getInstance().findById(i);

        if (z == null) {
            System.out.println("Drink s týmto ID neexistuje.");
            return;
        }
        VypisMenu.getInstance().printSkladoveZasoby(z);
    }

    private void vypisOblubene() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj svoje zákaznícke ID: ");
        int i = Integer.parseInt(br.readLine());

        List<Oblubene> z = NajdiOblubene.getInstance().findTop3(i);

        if(z.size() == 0) {
            System.out.println("Ešte nemáte žiadne oblubene napoje.");
            return;
        }

        int a = 1;
        for (Oblubene n : z) {
            if (n == null) {
                System.out.println("Oblubeny nápoj neexistuje.");
            } else {
                System.out.println(a++ + ".");
                VypisOblubene.getInstance().print(n);
            }
        }
    }

    private void navstivenePodniky() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadaj svoje ID: ");
        int i = Integer.parseInt(br.readLine());

        List<Integer> z = NajdiPodnik.getInstance().findNAvstevy(i);

        if(z.size() == 0) {
            System.out.println("Ešte si nikde nebol.");
            return;
        }

        for (Integer n : z) {
            if (n == null) {
                System.out.println("Podnik neexistuje.");
            } else {
                Podnik p = NajdiPodnik.getInstance().findById(n);
                VypisNavstevy.getInstance().print(p);
            }
        }
    }

    private void vypisZNP() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("3tatistika ludi co si napoj objednali v každom podniku, kde ho majú.");

        List<Ucet> z = NajdiUcet.getInstance().statistikaZNP();

        if(z.size() == 0) {
            System.out.println("Nápoj nemá takú štatistiku.");
            return;
        }

        for (Ucet n : z) {
            if (n == null) {
                System.out.println("neexistuje.");
            } else {
                VypisZNP.getInstance().print(n);
            }
        }
    }

    public void platba() throws IOException, SQLException, ObjednavkaException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Kto bude platiť účet? ID: ");
        int zakaznik = Integer.parseInt(br.readLine());
        System.out.println("ID stolíka: ");
        int stol = Integer.parseInt(br.readLine());
        System.out.println("Podla vypísanej objednavky zvol nápoje, za ktoré budete platiť.");
        System.out.println("Výber zapíšte v tvare 'idObjednavka1,idObjednavka2,...' .");
        String vyber = (br.readLine());
        List<String> id_napojov = new ArrayList<String>(Arrays.asList(vyber.split(",")));

        double suma = 0; List<Objednavky> o = new ArrayList<Objednavky>();
        Zakaznik zak = NajdiZakazika.getInstance().findById(zakaznik);

        // kontrola zakaznika
        if (zak == null) { return;}
        Karta karta = NajdiKarta.getInstance().findByZakaznik(zakaznik);
        double zlava = karta.getId() == null? 0 : karta.getZlava_percenta();

        // vytvori  sa zoznam objednavok ktore chcem spracovať
        for (String n : id_napojov){
             int n2 = Integer.parseInt(n);
             Objednavky objednavky = NajdiObjednavky.getInstance().findById(n2);
             double poZlave = NajdiNapoj.getInstance().findById(objednavky.getId_napoj()).getPredajna_cena();
             poZlave -= poZlave*zlava;
             suma += poZlave;

            o.add(objednavky);
        }

        // transakcia
        List<Ucet> ucet = VykonajObjednavka.getInstance().vytvoreneO(zakaznik, stol, o);

        if(ucet.isEmpty())  {
            System.out.println("Udaje neboli zadane spravne.");
            return;
        }
        else {
            System.out.println("OK Váš výber je zaznamenaný.");
            System.out.println("Celková cena je " + suma + ". Chcete zaplatit? A / N");
            String odpoved = (br.readLine());

            if (odpoved.equals("A")){
                boolean k = PotvrdObjednavka.getInstance().zaplat(zakaznik, suma, ucet, o);
                if (k)
                    System.out.println("Uspesne zaplatene.");
                else
                    System.out.println("Nepodarilo sa zaplatit.");
            }
        }
    }

    public void vymazObjednavka() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Vyber id objednavky, krorú chceš zrušiť.");
        int id_obj = Integer.parseInt(br.readLine());

        Objednavky o = NajdiObjednavky.getInstance().findById(id_obj);

        if (o == null) {
            System.out.println("Napoj neexistuje");
        } else {
            try {
                o.delete();
                System.out.println("Objednavka uspesne vymazana.");
            } catch (Exception e){
                System.out.println("Nemozno vymazat.");
            }

        }
    }

    private void napojMesiaca() throws SQLException {
        List<StatistikaNapojMesiac> zoz = NajdiStatNapojMesiac.getInstance().statistikaNapojMesiaca();

        if (zoz.isEmpty()) {
            System.out.println("Neexistuje.");
            return;
        }

        for (StatistikaNapojMesiac n : zoz) {
            if (n == null) {
                System.out.println("Nastala chyba v hladaní tržby.");
            } else {
                VypisStatistikaNM.getInstance().print(n);
            }
        }
    }
}

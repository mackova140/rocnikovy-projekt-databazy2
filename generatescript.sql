/**
 * Author:  Natalia Mackova
 * Created: marec, 2021
 */

-- Najprv zmazeme vsetky data.
truncate table velkosklad, podnik, napojovy_listok, konto_zakaznika, oblubene, karta, konto_prevadzkovatel, objednavky, ucet restart identity cascade;

---- POMOCNE tabulky na generovanie dat

-- mena pouzivatelov
drop table if exists mena cascade;
create table mena
(meno_pouzivatela varchar);

insert into mena (meno_pouzivatela)
values	('Karol'), ('Fero'), ('Hektor'), ('Amanda'), ('Stiv'),
          ('Adam'), ('Rudolf'), ('Liliana'), ('Emilia'), ('Kika'),
          ('Lenka'), ('Michaela'), ('Zita'), ('Paula'), ('Patrik'),
          ('Michael'), ('Nikolas'), ('Izidor'), ('Klement'), ('Koloman'),
          ('Daniela'), ('Rea'), ('Alan'), ('Verona'), ('Harry');

-- nazvy podnikov
drop table if exists nazov_podnik cascade;
create table nazov_podnik
(
    nazov varchar
);

insert into nazov_podnik (nazov)
values	('Thalmainer'), ('My Coffe'), ('Tyffanies'), ('Hedon'), ('La Crema'),
          ('Verdon'), ('De Nuevo'), ('Kavieren'), ('Vinaren'), ('Pub'),
       ('My Barista Time'), ('Le Papilon'), ('Tea shop'), ('Coffe Žofia'), ('Pubik 21');

-- nazvy napojov
drop table if exists nazov_napoj cascade;
create table nazov_napoj
(
    nazov varchar
);

insert into nazov_napoj (nazov)
values	('Pivo svetlé'), ('Pivo tmave'), ('Pivo special'), ('vino biele'), ('vino červené'),
          ('vino ružové'), ('vino šumivé'), ('capucino'), ('esspreso'), ('latté'),
          ('ristreto'), ('matcha latté'), ('karamelové frappe'), ('orieškové latté'), ('horúca čokoláda'),
          ('kakao'), ('limonáda malina'), ('limonáda citrón'), ('limonáda malina'), ('limonáda mango'),
          ('kofola'), ('mača čaj'), ('BOBA tea'), ('vodka'), ('Tatra Tea');

-- kategorie kariet

drop table if exists kategorie cascade;     -- todo spravit priamo v selecte
create table kategorie
(
    k varchar
);

insert into kategorie (k)
values	('zlatá'), ('strieborná'), ('bronzová');

drop table if exists jednotka cascade;
create table jednotka
(k varchar);

insert into jednotka (k)
values	('ml'), ('l'), ('dl');

-- FUNKCIE
create or replace function random_napoj() returns varchar language sql as
$$
select nazov from nazov_napoj order by random() limit 1
$$;

create or replace function random_meno() returns varchar language sql as
$$
select meno_pouzivatela from mena order by random() limit 1
$$;

create or replace function random_podnik() returns varchar language sql as
$$
select nazov from nazov_podnik order by random() limit 1
$$;

create or replace function random_jednotka() returns varchar language sql as
$$
select k from jednotka order by random() limit 1
$$;

create or replace function random_kategoria() returns varchar language sql as
$$
select k from kategorie order by random() limit 1
$$;

-- HLAVNE TABULKY

-- KONTO
INSERT INTO konto_prevadzkovatel(meno, heslo, kredit)
select 'admin', 'admin', 1000000;

-- SKLAD
insert into velkosklad (nazov, nakupna_cena, objem, merna_jednotka)
select  random_napoj(),
        floor(random() * 10 + 2) as nakupna_cena,
        floor(random() * 20 + 2) as objem,
        random_jednotka() as merna_jednotka
from generate_series(1, 50) as seq(i);

-- ZAKAZNICI
insert into konto_zakaznika (meno, heslo, kredit)
select  random_meno() as nazov,
        'heslo' || floor(random() * 20 + 2) as heslo,
        floor(random() * (100-50) + 1000) as kredit
from generate_series(1, 80000) as seq(i);

-- PODNIKY
insert into podnik (nazov)
select  random_podnik() as nazov
from generate_series(1, 30) as seq(i);

-- NAPOJOVE LISTKY
with data as (
    select  s.i,
            floor(random() * 30 + 1) as id_podnik,
            s.id as id_napoj,
            s.nazov as nazov,
            s.nakupna_cena * 1.2 as predajna_cena,
            s.objem * 0.3 as objem,
            floor(random() * (100-50) + 10) as mnozstvo
    from (generate_series(1, 50) as seq(i)
             cross join lateral (select seq.i, * from velkosklad order by random() limit 1) as s)
    -- cross join lateral (select seq.i, * from podnik order by random() limit 1) as p
)

insert into napojovy_listok (id_podnik, id_napoj, nazov, predajna_cena, objem, mnozstvo)
select id_podnik, id_napoj, nazov, predajna_cena, objem, mnozstvo from data;


-- STOLY    treba dat pozor na to aby mal dany podnik napojovy listok
with data as (
    select  p1.i,
            floor(random() * (4) + 3) as pocet_miest,
            p1.id_podnik as id_podnik
    from generate_series(1, 300) as seq(i)
             cross join lateral (select seq.i, id_podnik from napojovy_listok order by random() limit 1) as p1
)

insert into stoly (pocet_miest, id_podnik)
select  pocet_miest, id_podnik from data;

-- OBJEDNAVKY
with data as (
    select  s.i,
            s.id as id_stol,
            (select n.id from podnik p inner join napojovy_listok n on p.id = s.id_podnik and n.id_podnik = s.id_podnik
             order by random() limit 1) as id_napoj
    from generate_series(1, 10000) as seq(i)
             cross join lateral (select seq.i, * from stoly order by random() limit 1) as s
)
insert into objednavky (id_stol, id_napoj)
select id_stol, id_napoj from data;

-- UCET
with data as (
    select  v.i,
            floor(random() * (DATE '2019-03-30' - DATE '2021-01-05'))::integer + DATE '2021-01-05' as datum,
            v.nakupna_cena * 1.2 as cena,       -- cena je z konkretneho podniku
            floor(random() * 30 + 1) as id_podnik,
            v.id as id_napoj,                   -- ale id_napoja je z velkoskladu
            floor(random() * 80000 + 1) as id_zakaznik
    from generate_series(1, 1000000) as seq(i)
             cross join lateral (select seq.i, * from velkosklad order by random() limit 1) as v
)

insert into ucet (datum, cena, id_podnik,id_zakaznik, id_napoj)
select datum, cena, id_podnik,id_zakaznik, id_napoj from data;

-- OBLUBENE NAPOJE
with data as (
    select  p1.i,
            p1.nazov as nazov,
            p1.id as id_napoj,
            floor(random() * 80000 + 1) as id_zakaznik,
            floor(random() * 20 + 1) as pocet
    from generate_series(1, 1000) as seq(i)
             cross join lateral (select seq.i, id, nazov from velkosklad order by random() limit 1) as p1
)

insert into oblubene (id_napoj, id_zakaznik, nazov, pocet)
select  id_napoj, id_zakaznik, nazov, pocet from data;

-- KARTY
with data as (
    select  i,
            i as id_zakaznik,
            random_kategoria() as kategoria
    from generate_series(1, 80000) as seq(i)
)

insert into karta (id_zakaznik, kategoria, zlava_percenta)
select  id_zakaznik,
        kategoria,
        CASE    WHEN kategoria = 'zlatá' THEN 0.20
                WHEN kategoria = 'strieborná' THEN 0.10
                ELSE 0.05
            END as zlava_percenta
from data;

-- NAKUPY podnikov, nepotrebujeme pre žiadnu štetistuku tak nemusime generovať vela
with data as (
    select  i,
            floor(random() * (DATE '2019-03-30' - DATE '2021-01-05'))::integer + DATE '2021-01-05' as datum,
            floor(random() * 200 + 20) as suma
    from generate_series(1, 20) as seq(i)
)

insert into nakupy (datum, suma)
select datum, suma from data;

---- nakoniec zmazeme pomocne veci
drop table mena, nazov_napoj, nazov_podnik, jednotka cascade;
drop function random_jednotka();
drop function random_meno();
drop function random_napoj();
drop function random_podnik();
drop function random_kategoria();
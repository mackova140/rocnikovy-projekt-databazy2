/**
 * Author:  Natalia Mackova
 * Created: marec, 2021
 */

drop table if exists velkosklad cascade;
create table velkosklad
(
    id serial primary key,
    nazov varchar,
    nakupna_cena numeric,
    objem numeric,
    merna_jednotka varchar
);

drop table if exists podnik cascade;
create table podnik
(
    id serial primary key,
    nazov varchar
);

drop table if exists napojovy_listok cascade;
create table napojovy_listok
(
    id serial primary key,
    id_napoj integer references velkosklad(id),
    id_podnik integer references podnik(id),
    nazov varchar,
    predajna_cena numeric,
    objem numeric,
    mnozstvo int
);

drop table if exists konto_zakaznika cascade;
create table konto_zakaznika
(
    id serial primary key,
    meno varchar,
    heslo varchar,
    kredit numeric
);

drop table if exists oblubene cascade;  -- oblubene napoje konkretneho zakaznika
create table oblubene
(
    id serial primary key,
    id_napoj integer references velkosklad(id),
    id_zakaznik integer references konto_zakaznika(id),
    nazov varchar,
    pocet integer
);


drop table if exists karta cascade;
create table karta
(
    id serial primary key,
    id_zakaznik integer references konto_zakaznika(id),     -- karta vie id zakaznika
    kategoria varchar,
    zlava_percenta numeric
);

drop table if exists konto_prevadzkovatel cascade;          -- todo: podmnozina
create table konto_prevadzkovatel
(
    -- id serial primary key, asi ani netreba
    meno varchar,
    heslo varchar,
    kredit numeric  -- nas zaujima hlavne stav uctu
);

drop table if exists nakupy cascade;    -- nakup vsetkych nasich podnikov sa strhava u konta prevadzkovatela
create table nakupy
(
    id serial primary key,                      -- todo aj id_podnik ????
    datum date,	-- datum pripisanie platby
    suma numeric
);

drop table if exists stoly cascade;
create table stoly
(
    id serial primary key,
    id_podnik integer references podnik(id),
    pocet_miest integer
);

drop table if exists objednavky cascade;            -- mesTrzba je vlastne zaplatena objednavka
create table objednavky
(
    id serial primary key,
    id_stol integer references stoly(id),
    id_napoj integer references napojovy_listok(id) on delete cascade  -- null
    -- id_zakaznik integer references konto_zakaznika(id)  -- zakaznika riesime az po platbe
);

drop table if exists ucet cascade;
create table ucet
(
    id serial primary key,
    -- stav varchar,                     podla mna je uz zaplateny
    cena numeric,                       -- cena podla zaplateneho
    datum date,
    -- id_objednavka integer references objednavky(id)                    -- takto vieme o aky podnik ide
    id_podnik integer references stoly(id),                               -- zistime vdaka stolu
    id_napoj integer references velkosklad(id),                           -- vdaka napojovemu listku vieme povodne id produktu
    id_zakaznik integer references konto_zakaznika(id)                    -- podla toho kto platil, mozno nahradim za kartu aby sme vedeli aj zlavu uplatnit
);

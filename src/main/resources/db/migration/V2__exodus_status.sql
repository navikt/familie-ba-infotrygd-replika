-- Styringstabell for replikering fra familie-ks-exodus (on-prem) til denne GCP-postgres-basen.
--
-- iterator er en cursor levert av exodus sitt /api/hentUttrekk-endepunkt, og brukes
-- til å be om neste side med data for en gitt tabell. job_status brukes til å registrere at exodus
-- har re-eksportert tabellen fra Oracle (NY_BASELINE), som gjør at den lokale iteratoren er ugyldig
-- og at tabellen må trunkeres og replikeres på nytt fra bunnen av.
create table exodus_status
(
    tabell              varchar(64) not null primary key,
    iterator            varchar,
    job_status          varchar(32) not null default 'OK',
    antall_rader_hentet bigint      not null default 0,
    sist_oppdatert      timestamp   not null default current_timestamp
);
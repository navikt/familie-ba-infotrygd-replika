alter table ba_barn_10
    add column b10_barn_fnr_normalisert varchar(11);

alter table ba_person_01
    add column fnr_normalisert varchar(11);

alter table ba_stoenad_20
    add column fnr_normalisert varchar(11);

alter table ba_utbetaling_30
    add column fnr_normalisert varchar(11);

alter table sa_hendelse_20
    add column fnr_normalisert varchar(11);

alter table sa_sak_10
    add column fnr_normalisert varchar(11);

alter table sa_status_15
    add column fnr_normalisert varchar(11);


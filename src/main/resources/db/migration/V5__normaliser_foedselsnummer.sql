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

alter table ba_barn_10
    add column b10_ba_iver_dato date;
alter table ba_barn_10
    add column b10_ba_vfom_dato date;
alter table ba_barn_10
    add column b10_ba_tom_dato date;

alter table ba_stoenad_20
    add column b20_iverfom_seq_dato date;
alter table ba_stoenad_20
    add column b20_virkfom_seq_dato date;

alter table ba_utbetaling_30
    add column b30_start_utbet_mnd_seq_dato date;
alter table ba_utbetaling_30
    add column b30_vfom_seq_dato date;

alter table sa_hendelse_20
    add column s20_aksjonsdato_seq_dato date;

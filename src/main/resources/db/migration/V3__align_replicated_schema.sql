alter table ba_barn_10
    add column db_splitt char(2) default 'BA';

alter table ba_person_01
    alter column id_ba_pers type numeric,
    add column db_splitt char(2) default 'BA';

alter table ba_stoenad_20
    add column db_splitt char(2) default 'BA';

alter table ba_utbetaling_30
    add column db_splitt char(2) default 'BA';

alter table sa_hendelse_20
    add column s20_s_b_kode char(1),
    add column s20_brevnummer char(2),
    add column s20_mottakerkode varchar(3),
    add column s20_mottakernr varchar(11),
    add column s20_fnr_anr_instnr numeric(11),
    add column s20_ant_purreuker_1 char(2),
    add column s20_ant_purreuker_2 char(2),
    add column s20_ant_purreuker_3 char(2),
    add column s20_s_purrenr char(1),
    add column s20_1_purredato numeric(8),
    add column s20_2_purredato numeric(8),
    add column s20_3_purredato numeric(8),
    add column s20_returdato numeric(8),
    add column s20_neste_purredato numeric(8),
    add column s20_kladd char(1),
    add column s20_lopenr_fm01 char(1),
    add column s20_lopenr_print varchar(3),
    add column s20_sentralprint char(1),
    add column s20_brevtype char(1),
    add column s20_tekstkode_2 varchar(4),
    add column s20_tekstkode_3 varchar(4),
    add column s20_tekstkode_4 varchar(4),
    add column s20_tekstkode_5 varchar(4),
    add column s20_purretekst_1 varchar(4),
    add column s20_purretekst_2 varchar(4),
    add column s20_purretekst_3 varchar(4),
    add column s20_lager char(1),
    add column s20_reg_av varchar(3),
    add column s20_oppr_tknr varchar(4),
    add column s20_oppr_brukerid varchar(7),
    add column s20_ikke_avbryt char(1),
    add column s20_maalform char(1),
    add column opprettet timestamp(6) default current_timestamp,
    add column endret_i_kilde timestamp(6) default current_timestamp,
    add column kilde_is varchar(12) default ' ',
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '99',
    alter column s20_saksnr type char(2);

alter table sa_sak_10
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '99';

alter table sa_status_15
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '99';

alter table t_beslut
    add column db_splitt char(2) default '  ';

alter table t_delytelse
    drop constraint t_delytelse_pkey,
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '  ',
    add primary key (vedtak_id, type_delytelse, tidspunkt_reg);

alter table t_endring
    alter column opprettet set default current_timestamp,
    alter column oppdatert set default current_timestamp,
    alter column db_splitt set default '  ',
    add primary key (vedtak_id, kode);

alter table t_lopenr_fnr
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '  ';

alter table t_stonad
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '  ';

alter table t_stonadsklasse
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '  ';

alter table t_vedtak
    add column oppdatert timestamp(6) default current_timestamp,
    add column db_splitt char(2) default '  ';

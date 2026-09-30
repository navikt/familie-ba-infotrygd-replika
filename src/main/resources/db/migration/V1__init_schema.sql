create table ba_barn_10
(
    b01_personkey    numeric(15, 0) not null,
    b10_barn_fnr     numeric(11, 0) not null,
    b10_ba_iver      varchar(6),
    b10_ba_vfom      varchar(6),
    b10_ba_tom       varchar(6)     not null,
    b10_stonads_type varchar(2),
    tk_nr            varchar(4)     not null,
    f_nr             varchar(11)    not null,
    opprettet        timestamp(6)   not null default current_timestamp,
    endret_i_kilde   timestamp(6)   not null default current_timestamp,
    kilde_is         varchar(12)    not null default ' ',
    region           char(1)        not null default ' ',
    id_ba_barn       numeric        not null primary key,
    oppdatert        timestamp(6)   not null default current_timestamp
);

create table ba_person_01
(
    b01_personkey       numeric(15, 0) not null,
    b01_bt_status       char(1),
    b01_mottaker_kode   varchar(3),
    b01_mottaker_nr     numeric(11, 0),
    b01_akonto_belop    numeric(7, 0),
    b01_pensjonstrygdet char(1),
    tk_nr               varchar(4)     not null,
    f_nr                varchar(11)    not null,
    opprettet           timestamp(6)   not null default current_timestamp,
    endret_i_kilde      timestamp(6)   not null default current_timestamp,
    kilde_is            varchar(12)    not null default ' ',
    region              char(1)        not null default ' ',
    id_ba_pers          numeric(15, 0) not null primary key,
    oppdatert           timestamp(6)   not null default current_timestamp
);

create table ba_stoenad_20
(
    b01_personkey     numeric(15, 0) not null,
    b20_iverfom_seq   varchar(6),
    b20_virkfom_seq   varchar(6),
    b20_gruppe        varchar(2),
    b20_brukerid      varchar(7),
    b20_tknr          varchar(4),
    b20_reg_dato      numeric(8, 0),
    b20_sok_dato      numeric(8, 0),
    b20_blokk         char(1),
    b20_sak_nr        varchar(2),
    b20_status        varchar(2),
    b20_tekstkode     varchar(2),
    b20_ebet_fom      varchar(6),
    b20_ebet_tom      varchar(6),
    b20_opphoert_iver varchar(6),
    b20_opphorsgrunn  char(1),
    b20_ant_barn      numeric(2, 0),
    b20_omregn        char(1),
    b20_eos           char(1),
    b20_ekstra_smab   char(1),
    b20_svalbard      char(1),
    b20_samboertype   char(1),
    b20_opphoert_vfom varchar(6)     not null,
    b20_ant_smaabarn  numeric(2, 0),
    b20_delt_oms_pros varchar(2),
    tk_nr             varchar(4)     not null,
    f_nr              varchar(11)    not null,
    opprettet         timestamp(6)   not null default current_timestamp,
    endret_i_kilde    timestamp(6)   not null default current_timestamp,
    kilde_is          varchar(12)    not null default ' ',
    region            char(1)        not null default ' ',
    id_ba_stoenad     numeric        not null primary key,
    oppdatert         timestamp(6)   not null default current_timestamp
);


create table ba_utbetaling_30
(
    b01_personkey           numeric(15, 0) not null,
    b30_start_utbet_mnd_seq varchar(6)     not null,
    b30_vfom_seq            varchar(6)     not null,
    b30_kontonr             varchar(8)     not null,
    b30_utbet_type          char(1)        not null,
    b30_gruppe              varchar(2),
    b30_brukerid            varchar(7),
    b30_utbet_fom           varchar(6),
    b30_utbet_tom           varchar(6),
    b30_utbetalt            char(1),
    b30_belop               numeric(7, 0)  not null,
    b30_utbet_dato          numeric(8, 0),
    b30_kode                char(1),
    tk_nr                   varchar(4),
    f_nr                    varchar(11)    not null,
    opprettet               timestamp(6)            default current_timestamp,
    endret_i_kilde          timestamp(6)            default current_timestamp,
    kilde_is                varchar(12)             default ' ',
    region                  char(1)        not null default ' ',
    id_ba_utbet             numeric        not null primary key,
    oppdatert               timestamp(6)            default current_timestamp
);

create table sa_hendelse_20
(
    id_hend             numeric        not null primary key,
    s01_personkey       numeric(15, 0) not null,
    s05_saksblokk       char(1)        not null,
    s20_aksjonsdato_seq numeric(8, 0)  not null,
    s20_opplysning      varchar(38),
    s20_saksnr          varchar(2)     not null,
    s20_tekstkode_1     varchar(4)     not null,
    s20_dato_dannet     numeric(8, 0),
    tk_nr               varchar(4)     not null,
    f_nr                varchar(11)    not null,
    region              char(1)        not null default ' '
);


create table sa_sak_10
(
    s01_personkey          numeric(15),
    s05_saksblokk          char(1),
    s10_saksnr             char(2),
    s10_reg_dato           numeric(8),
    s10_mottattdato        numeric(8),
    s10_kapittelnr         char(2),
    s10_valg               char(2),
    s10_undervalg          char(2),
    s10_dublett_feil       char(1),
    s10_type               char(2),
    s10_innstilling        char(2),
    s10_resultat           char(2),
    s10_nivaa              char(3),
    s10_innstilldato       numeric(8),
    s10_vedtaksdato        numeric(8),
    s10_iverksattdato      numeric(8),
    s10_grunnbl_dato       numeric(8),
    s10_aarsakskode        char(2),
    s10_tellepunkt         char(3),
    s10_telletype          char(1),
    s10_telledato          numeric(8),
    s10_eval_kode          char(4),
    s10_eval_tir           char(1),
    s10_fremlegg           char(3),
    s10_innstilling2       char(2),
    s10_innstilldato2      numeric(8),
    s10_annen_instans      char(1),
    s10_behen_type         char(3),
    s10_behen_enhet        char(4),
    s10_reg_av_type        char(3),
    s10_reg_av_enhet       char(4),
    s10_diff_framlegg      char(3),
    s10_innstillt_av_type  char(3),
    s10_innstillt_av_enhet char(4),
    s10_vedtatt_av_type    char(3),
    s10_vedtatt_av_enhet   char(4),
    s10_prio_tab           char(6),
    s10_aoe                char(3),
    s10_es_system          char(1),
    s10_es_gsak_oppdragsid numeric(10),
    s10_knyttet_til_sak    char(2),
    s10_vedtakstype        char(1),
    s10_reell_enhet        char(4),
    s10_mod_endret         char(1),
    tk_nr                  char(4),
    f_nr                   char(11),
    opprettet              timestamp(6) default current_timestamp,
    endret_i_kilde         timestamp(6) default current_timestamp,
    kilde_is               varchar(12)  default ' ',
    region                 char(1)      default ' ',
    id_sak                 numeric not null primary key
);


create table sa_status_15
(
    s01_personkey       numeric(15),
    s05_saksblokk       char(1),
    s10_saksnr          char(2),
    s15_lopenr          char(2),
    s15_beh_enhet_type  char(3),
    s15_beh_enhet_enhet char(4),
    s15_status          char(2),
    s15_status_dato     numeric(8),
    s15_brukerid        char(7),
    s15_status_klokke   char(6),
    s15_status_brukerid char(7),
    s15_endrings_kode   char(1),
    s15_type_gml        char(2),
    s15_type_ny         char(2),
    s15_lovetsvar_dato  numeric(8),
    s15_ant_lofter      char(2),
    s15_gruppe          char(2),
    s15_sperr           char(1),
    tk_nr               char(4),
    f_nr                char(11),
    opprettet           timestamp(6) default current_timestamp,
    endret_i_kilde      timestamp(6) default current_timestamp,
    kilde_is            varchar(12)  default ' ',
    region              char(1)      default ' ',
    id_status           numeric not null primary key
);


create table t_beslut
(
    beslutning_id  numeric not null primary key,
    vedtak_id      numeric not null,
    saksbehandler1 varchar(8),
    godkjent1      char(1),
    enhet1         varchar(4),
    tidspunkt_reg1 timestamp(6),
    saksbehandler2 varchar(8),
    godkjent2      char(1),
    enhet2         varchar(4),
    tidspunkt_reg2 timestamp(6),
    sendt_til_os   timestamp(6),
    mottatt_fra_os timestamp(6),
    godkjent_av_os char(1),
    opprettet      timestamp(6) default current_timestamp,
    oppdatert      timestamp(6) default current_timestamp
);

create table t_delytelse
(
    vedtak_id       numeric      not null,
    type_delytelse  char(2)      not null,
    tidspunkt_reg   timestamp(6),
    fom             date,
    tom             date,
    belop           numeric(11, 2),
    oppgjorsordning char(1),
    mottaker_lopenr numeric,
    brukerid        char(8),
    type_sats       char(1),
    type_utbetaling char(1),
    linje_id        numeric      not null,
    opprettet       timestamp(6) not null default current_timestamp,
    primary key (vedtak_id, linje_id)
);

create table t_endring
(
    vedtak_id numeric(38, 0),
    kode      char(2),
    opprettet timestamp(6),
    oppdatert timestamp(6),
    db_splitt char(2)
);

create table t_lopenr_fnr
(
    person_lopenr numeric      not null primary key,
    personnr      char(11)     not null,
    opprettet     timestamp(6) not null default current_timestamp
);

create table t_stonad
(
    stonad_id         numeric      not null primary key,
    person_lopenr     numeric,
    kode_rutine       char(2),
    dato_start        date,
    kode_opphor       char(2),
    dato_opphor       date,
    oppdrag_id        numeric,
    tidspunkt_opphort timestamp(6),
    tidspunkt_reg     timestamp(6),
    brukerid          char(8),
    opprettet         timestamp(6) not null default current_timestamp
);

create table t_stonadsklasse
(
    vedtak_id   numeric    not null,
    kode_nivaa  varchar(2) not null,
    kode_klasse varchar(2) not null,
    opprettet   timestamp(6) default current_timestamp,
    primary key (vedtak_id, kode_nivaa, kode_klasse)
);

create table t_vedtak
(
    vedtak_id           numeric      not null primary key,
    person_lopenr       numeric      not null,
    kode_rutine         char(2),
    dato_start          date,
    tknr                char(4),
    saksblokk           char(1),
    saksnr              numeric,
    type_sak            char(2),
    kode_resultat       char(2),
    dato_innv_fom       date,
    dato_innv_tom       date,
    dato_mottatt_sak    date,
    kode_vedtaksnivaa   char(3),
    type_beregning      char(3),
    tknr_beh            char(4),
    tidspunkt_reg       timestamp(6),
    brukerid            char(8),
    nokkel_dl1          char(30),
    alternativ_mottaker numeric(11),
    stonad_id           numeric,
    kidnr               varchar(25),
    faktnr              varchar(33),
    opprettet           timestamp(6) not null default current_timestamp
);

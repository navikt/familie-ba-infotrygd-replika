package no.nav.familie.ba.infotrygd.replika.exodus

/**
 * Alle tabellene som skal repliseres fra familie-ks-exodus, med den naturlige nøkkelen fra Oracle
 * som brukes til å upserte rader i Postgres. Kolonnenavn er alltid lowercase siden det er slik
 * Postgres folder unquoted identifiers.
 */
enum class ExodusTabell(
    val tabellNavn: String,
    val primærnøkkel: List<String>,
) {
    BA_BARN_10("ba_barn_10", listOf("id_ba_barn")),
    BA_PERSON_01("ba_person_01", listOf("id_ba_pers")),
    BA_STOENAD_20("ba_stoenad_20", listOf("id_ba_stoenad")),
    BA_UTBETALING_30("ba_utbetaling_30", listOf("id_ba_utbet")),
    SA_HENDELSE_20("sa_hendelse_20", listOf("id_hend")),
    SA_SAK_10("sa_sak_10", listOf("id_sak")),
    SA_STATUS_15("sa_status_15", listOf("id_status")),
    T_BESLUT("t_beslut", listOf("beslutning_id")),
    T_DELYTELSE("t_delytelse", listOf("vedtak_id", "type_delytelse", "tidspunkt_reg")),
    T_LOPENR_FNR("t_lopenr_fnr", listOf("person_lopenr")),
    T_STONAD("t_stonad", listOf("stonad_id")),
    T_STONADSKLASSE("t_stonadsklasse", listOf("vedtak_id", "kode_nivaa", "kode_klasse")),
    T_VEDTAK("t_vedtak", listOf("vedtak_id")),
    ;

    companion object {
        fun fraTabellNavn(navn: String): ExodusTabell =
            entries.find { it.tabellNavn.equals(navn, ignoreCase = true) }
                ?: throw IllegalArgumentException("Ukjent exodus-tabell: $navn")
    }
}

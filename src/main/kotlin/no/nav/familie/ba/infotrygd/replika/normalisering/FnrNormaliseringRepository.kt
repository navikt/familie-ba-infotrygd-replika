package no.nav.familie.ba.infotrygd.replika.normalisering

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

enum class FnrKolonne(
    val tabellnavn: String,
    val kildekolonne: String,
    val målkolonne: String,
) {
    BA_BARN_10("ba_barn_10", "b10_barn_fnr", "b10_barn_fnr_normalisert"),
    BA_PERSON_01("ba_person_01", "f_nr", "fnr_normalisert"),
    BA_STOENAD_20("ba_stoenad_20", "f_nr", "fnr_normalisert"),
    BA_UTBETALING_30("ba_utbetaling_30", "f_nr", "fnr_normalisert"),
    SA_HENDELSE_20("sa_hendelse_20", "f_nr", "fnr_normalisert"),
    SA_SAK_10("sa_sak_10", "f_nr", "fnr_normalisert"),
    SA_STATUS_15("sa_status_15", "f_nr", "fnr_normalisert"),
}

@Repository
class FnrNormaliseringRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun normaliser(fnrKolonne: FnrKolonne): Int =
        jdbcTemplate.update(
            """
            update ${fnrKolonne.tabellnavn}
            set ${fnrKolonne.målkolonne} =
                substring(lpad(btrim(${fnrKolonne.kildekolonne}::text), 11, '0'), 5, 2) ||
                substring(lpad(btrim(${fnrKolonne.kildekolonne}::text), 11, '0'), 3, 2) ||
                substring(lpad(btrim(${fnrKolonne.kildekolonne}::text), 11, '0'), 1, 2) ||
                substring(lpad(btrim(${fnrKolonne.kildekolonne}::text), 11, '0'), 7, 5)
            where ${fnrKolonne.kildekolonne} is not null
              and ${fnrKolonne.målkolonne} is null
              and btrim(${fnrKolonne.kildekolonne}::text) ~ '^[0-9]{8,11}$'
            """,
        )
}

package no.nav.familie.ba.infotrygd.replika.normalisering

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/** Seq-verdier er komplementverdier: 999999 - YYYYMM eller 99999999 - YYYYMMDD. */
enum class SeqDatoFormat {
    YYYYMM,
    YYYYMMDD,
}

enum class SeqDatoKolonne(
    val tabellnavn: String,
    val primærnøkkel: String,
    val kildekolonne: String,
    val målkolonne: String,
    val format: SeqDatoFormat = SeqDatoFormat.YYYYMM,
) {
    BA_BARN_10_IVER("ba_barn_10", "id_ba_barn", "b10_ba_iver", "b10_ba_iver_dato"),
    BA_BARN_10_VFOM("ba_barn_10", "id_ba_barn", "b10_ba_vfom", "b10_ba_vfom_dato"),
    BA_BARN_10_TOM("ba_barn_10", "id_ba_barn", "b10_ba_tom", "b10_ba_tom_dato"),
    BA_STOENAD_20_IVERFOM("ba_stoenad_20", "id_ba_stoenad", "b20_iverfom_seq", "b20_iverfom_seq_dato"),
    BA_STOENAD_20_VIRKFOM("ba_stoenad_20", "id_ba_stoenad", "b20_virkfom_seq", "b20_virkfom_seq_dato"),
    BA_UTBETALING_30_START_UTBET_MND("ba_utbetaling_30", "id_ba_utbet", "b30_start_utbet_mnd_seq", "b30_start_utbet_mnd_seq_dato"),
    BA_UTBETALING_30_VFOM("ba_utbetaling_30", "id_ba_utbet", "b30_vfom_seq", "b30_vfom_seq_dato"),
    SA_HENDELSE_20_AKSJONSDATO("sa_hendelse_20", "id_hend", "s20_aksjonsdato_seq", "s20_aksjonsdato_seq_dato", SeqDatoFormat.YYYYMMDD),
}

@Repository
class SeqDatoRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun konverter(seqDatoKolonne: SeqDatoKolonne): Int {
        val navn = "${seqDatoKolonne.tabellnavn}.${seqDatoKolonne.målkolonne}"
        val start = System.nanoTime()
        logger.info("Starter konvertering av $navn med datoformat ${seqDatoKolonne.format}")
        val antallOppdatert =
            try {
                when (seqDatoKolonne.format) {
                    SeqDatoFormat.YYYYMM -> konverterÅrMåned(seqDatoKolonne)
                    SeqDatoFormat.YYYYMMDD -> konverterÅrMånedDag(seqDatoKolonne)
                }
            } catch (e: Exception) {
                logger.error("Konvertering av $navn feilet", e)
                throw e
            }
        val tidsbrukSekunder = (System.nanoTime() - start) / 1_000_000_000
        logger.info("Ferdig med konvertering av $navn: $antallOppdatert verdier oppdatert, tidsbruk $tidsbrukSekunder sekunder")
        return antallOppdatert
    }

    private fun konverterÅrMåned(kolonne: SeqDatoKolonne): Int {
        val kilde = "trim(${kolonne.kildekolonne}::text)"

        return jdbcTemplate.update(
            """
            update ${kolonne.tabellnavn} mål
            set ${kolonne.målkolonne} = to_date(k.datotall::text, 'YYYYMM')
            from (
                select ${kolonne.primærnøkkel},
                       case when $kilde ~ '^[0-9]{1,6}$'
                            then 999999 - $kilde::bigint
                       end as datotall
                from ${kolonne.tabellnavn}
                where ${kolonne.kildekolonne} is not null
                  and ${kolonne.målkolonne} is null
            ) k
            where mål.${kolonne.primærnøkkel} = k.${kolonne.primærnøkkel}
              and k.datotall between 190001 and 299912
              and k.datotall % 100 between 1 and 12
            """,
        )
    }

    private fun konverterÅrMånedDag(kolonne: SeqDatoKolonne): Int {
        val kilde = "trim(${kolonne.kildekolonne}::text)"
        val måned = "k.datotall / 100 % 100"
        val dag = "k.datotall % 100"

        return jdbcTemplate.update(
            """
            update ${kolonne.tabellnavn} mål
            set ${kolonne.målkolonne} = to_date(k.datotall::text, 'YYYYMMDD')
            from (
                select ${kolonne.primærnøkkel},
                       case when $kilde ~ '^[0-9]{1,8}$'
                            then 99999999 - $kilde::bigint
                       end as datotall
                from ${kolonne.tabellnavn}
                where ${kolonne.kildekolonne} is not null
                  and ${kolonne.målkolonne} is null
            ) k
            where mål.${kolonne.primærnøkkel} = k.${kolonne.primærnøkkel}
              and k.datotall between 19000101 and 29991231
              and $måned between 1 and 12
              and $dag between 1 and 31
            """,
        )
    }
}

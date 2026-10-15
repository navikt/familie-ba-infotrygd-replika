package no.nav.familie.ba.infotrygd.replika.normalisering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@JdbcTest
@Import(SeqDatoRepository::class)
@ActiveProfiles("test")
class SeqDatoRepositoryTest {
    @Autowired
    private lateinit var repository: SeqDatoRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @ParameterizedTest
    @EnumSource(SeqDatoKolonne::class)
    fun `konverterer alle datokolonner og lar allerede konverterte verdier stå`(kolonne: SeqDatoKolonne) {
        val kilde = if (kolonne.format == SeqDatoFormat.YYYYMM) "799791" else "79979184"
        val forventet = if (kolonne.format == SeqDatoFormat.YYYYMM) LocalDate.of(2002, 8, 1) else LocalDate.of(2002, 8, 15)
        settInn(kolonne, 1, kilde)

        val antallOppdatert = repository.konverter(kolonne)

        assertThat(antallOppdatert).isEqualTo(1)
        assertThat(hentDato(kolonne, 1)).isEqualTo(forventet)
        assertThat(repository.konverter(kolonne)).isZero()
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = ["      ", "ABCDEF", "999999", "000000", "790086", "799799", "-12345"])
    fun `hopper over ugyldige månedsverdier`(kilde: String?) {
        val kolonne = SeqDatoKolonne.BA_BARN_10_IVER
        settInn(kolonne, 1, kilde)

        assertThat(repository.konverter(kolonne)).isZero()
        assertThat(hentDato(kolonne, 1)).isNull()
    }

    @ParameterizedTest
    @ValueSource(strings = ["99999999", "00000000", "79978684", "79979199", "79979167"])
    fun `hopper over aksjonsdato med ugyldig år måned eller dag`(kilde: String) {
        val kolonne = SeqDatoKolonne.SA_HENDELSE_20_AKSJONSDATO
        settInn(kolonne, 1, kilde.toLong())

        assertThat(repository.konverter(kolonne)).isZero()
        assertThat(hentDato(kolonne, 1)).isNull()
    }

    @Test
    fun `overskriver ikke eksisterende dato`() {
        val kolonne = SeqDatoKolonne.BA_BARN_10_IVER
        val eksisterende = LocalDate.of(1999, 1, 1)
        settInn(kolonne, 1, "799791", eksisterende)

        assertThat(repository.konverter(kolonne)).isZero()
        assertThat(hentDato(kolonne, 1)).isEqualTo(eksisterende)
    }

    @Test
    fun `konverterer alle gyldige rader i valgt kolonne uten å endre andre kolonner`() {
        val kolonne = SeqDatoKolonne.BA_BARN_10_IVER
        settInn(kolonne, 1, "ugyldi")
        settInn(kolonne, 3, null)
        settInn(kolonne, 10, "799791")
        settInn(kolonne, 20, "799792")
        settInn(kolonne, 30, "799793")

        assertThat(repository.konverter(kolonne)).isEqualTo(3)
        assertThat(hentDato(kolonne, 1)).isNull()
        assertThat(hentDato(kolonne, 3)).isNull()
        assertThat(hentDato(kolonne, 10)).isEqualTo(LocalDate.of(2002, 8, 1))
        assertThat(hentDato(kolonne, 20)).isEqualTo(LocalDate.of(2002, 7, 1))
        assertThat(hentDato(kolonne, 30)).isEqualTo(LocalDate.of(2002, 6, 1))
        assertThat(hentDato(SeqDatoKolonne.BA_BARN_10_TOM, 10)).isNull()
    }

    private fun settInn(
        kolonne: SeqDatoKolonne,
        id: Long,
        kilde: Any?,
        dato: LocalDate? = null,
    ) {
        val verdier =
            linkedMapOf<String, Any?>(
                kolonne.primærnøkkel to id,
                "f_nr" to "01020312345",
                "tk_nr" to "0000",
            )
        when (kolonne.tabellnavn) {
            "ba_barn_10" -> {
                verdier.putAll(mapOf("b01_personkey" to 1, "b10_barn_fnr" to 1020312345L, "b10_ba_tom" to "999999"))
            }

            "ba_stoenad_20" -> {
                verdier.putAll(mapOf("b01_personkey" to 1, "b20_opphoert_vfom" to "000000"))
            }

            "ba_utbetaling_30" -> {
                verdier.putAll(
                    mapOf(
                        "b01_personkey" to 1,
                        "b30_start_utbet_mnd_seq" to "999999",
                        "b30_vfom_seq" to "999999",
                        "b30_kontonr" to "12345678",
                        "b30_utbet_type" to "A",
                        "b30_belop" to 100,
                    ),
                )
            }

            "sa_hendelse_20" -> {
                verdier.putAll(mapOf("s01_personkey" to 1, "s05_saksblokk" to "A", "s20_saksnr" to "01", "s20_tekstkode_1" to "0001"))
            }
        }
        verdier[kolonne.kildekolonne] = if (kolonne.format == SeqDatoFormat.YYYYMMDD && kilde is String) kilde.toLong() else kilde
        verdier[kolonne.målkolonne] = dato
        jdbcTemplate.update(
            "insert into ${kolonne.tabellnavn} (${verdier.keys.joinToString()}) values (${verdier.keys.joinToString { "?" }})",
            *verdier.values.toTypedArray(),
        )
    }

    private fun hentDato(
        kolonne: SeqDatoKolonne,
        id: Long,
    ): LocalDate? =
        jdbcTemplate.queryForObject(
            "select ${kolonne.målkolonne} from ${kolonne.tabellnavn} where ${kolonne.primærnøkkel} = ?",
            LocalDate::class.java,
            id,
        )
}

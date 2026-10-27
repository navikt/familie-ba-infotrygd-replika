package no.nav.familie.ba.infotrygd.replika.normalisering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@JdbcTest(properties = ["spring.datasource.url=jdbc:tc:postgresql:18.6://localhost/fnr-normalisering"])
@Import(FnrNormaliseringRepository::class)
@ActiveProfiles("test")
class FnrNormaliseringRepositoryTest {
    @Autowired
    private lateinit var repository: FnrNormaliseringRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @ParameterizedTest(name = "kolonne={0}, kildeverdi={1}, forventet={2}")
    @MethodSource("alleKolonnerOgLengder")
    fun `normaliserer fødselsnummer for alle kolonner og lengder`(
        kolonne: FnrKolonne,
        kildeverdi: String,
        forventet: String,
    ) {
        settInn(kolonne, kildeverdi)

        assertThat(repository.normaliser(kolonne)).isEqualTo(1)
        assertThat(hentNormalisert(kolonne)).isEqualTo(forventet)
        assertThat(repository.normaliser(kolonne)).isZero()
    }

    @Test
    fun `hopper over ugyldig fødselsnummer`() {
        settInn(FnrKolonne.SA_SAK_10, "1234567A")

        assertThat(repository.normaliser(FnrKolonne.SA_SAK_10)).isZero()
        assertThat(hentNormalisert(FnrKolonne.SA_SAK_10)).isNull()
    }

    @Test
    fun `overskriver ikke allerede normalisert fødselsnummer`() {
        settInn(FnrKolonne.SA_STATUS_15, "01020312345", "eksisterer")

        assertThat(repository.normaliser(FnrKolonne.SA_STATUS_15)).isZero()
        assertThat(hentNormalisert(FnrKolonne.SA_STATUS_15)).isEqualTo("eksisterer")
    }

    private fun settInn(
        kolonne: FnrKolonne,
        kildeverdi: String,
        normalisert: String? = null,
    ) {
        val (idKolonne, øvrigeKolonner, øvrigeVerdier) =
            when (kolonne) {
                FnrKolonne.BA_BARN_10 -> {
                    Triple("id_ba_barn", "b01_personkey, b10_ba_tom, tk_nr, f_nr", "1, '999999', '0000', '01020312345'")
                }

                FnrKolonne.BA_PERSON_01 -> {
                    Triple("id_ba_pers", "b01_personkey, tk_nr", "1, '9999'")
                }

                FnrKolonne.BA_STOENAD_20 -> {
                    Triple("id_ba_stoenad", "b01_personkey, b20_opphoert_vfom, tk_nr", "1, '000000', '9999'")
                }

                FnrKolonne.BA_UTBETALING_30 -> {
                    Triple(
                        "id_ba_utbet",
                        "b01_personkey, b30_start_utbet_mnd_seq, b30_vfom_seq, b30_kontonr, b30_utbet_type, b30_belop",
                        "1, '000000', '000000', '12345678', 'A', 100",
                    )
                }

                FnrKolonne.SA_HENDELSE_20 -> {
                    Triple(
                        "id_hend",
                        "s01_personkey, s05_saksblokk, s20_aksjonsdato_seq, s20_saksnr, s20_tekstkode_1, tk_nr",
                        "1, 'A', 20200101, '01', '0001', '9999'",
                    )
                }

                FnrKolonne.SA_SAK_10 -> {
                    Triple("id_sak", "s01_personkey", "1")
                }

                FnrKolonne.SA_STATUS_15 -> {
                    Triple("id_status", "s01_personkey", "1")
                }
            }
        jdbcTemplate.update(
            """
            insert into ${kolonne.tabellnavn} ($idKolonne, $øvrigeKolonner, ${kolonne.kildekolonne}, ${kolonne.målkolonne})
            values (1, $øvrigeVerdier, ${if (kolonne == FnrKolonne.BA_BARN_10) "cast(? as numeric)" else "?"}, ?)
            """,
            kildeverdi,
            normalisert,
        )
    }

    private fun hentNormalisert(kolonne: FnrKolonne): String? =
        jdbcTemplate.queryForObject(
            "select ${kolonne.målkolonne} from ${kolonne.tabellnavn} where ${idKolonne(kolonne)} = 1",
            String::class.java,
        )

    private fun idKolonne(kolonne: FnrKolonne): String =
        when (kolonne) {
            FnrKolonne.BA_BARN_10 -> "id_ba_barn"
            FnrKolonne.BA_PERSON_01 -> "id_ba_pers"
            FnrKolonne.BA_STOENAD_20 -> "id_ba_stoenad"
            FnrKolonne.BA_UTBETALING_30 -> "id_ba_utbet"
            FnrKolonne.SA_HENDELSE_20 -> "id_hend"
            FnrKolonne.SA_SAK_10 -> "id_sak"
            FnrKolonne.SA_STATUS_15 -> "id_status"
        }

    companion object {
        @JvmStatic
        fun alleKolonnerOgLengder(): Stream<Arguments> =
            FnrKolonne.entries
                .flatMap { kolonne ->
                    listOf(
                        Arguments.of(kolonne, "10156789", "01010056789"),
                        Arguments.of(kolonne, "121556789", "15120056789"),
                        Arguments.of(kolonne, "1020312345", "03020112345"),
                        Arguments.of(kolonne, "01020312345", "03020112345"),
                    )
                }.stream()
    }
}

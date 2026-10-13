package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.familie.ba.infotrygd.replika.model.db2.Vedtak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VedtakRepository : JpaRepository<Vedtak, Long> {
    /**
     * JPQL. Henter vedtakene på saken med [saksblokk] og [saksnummer] for personen [fnr].
     * Fødselsnummeret slås opp til personløpenummer via `LøpeNrFnr` (T_LOPENR_FNR).
     */
    @Query(
        """
        SELECT v FROM Vedtak v
            INNER JOIN LøpeNrFnr l
            ON v.løpenummer = l.personLøpenummer
        WHERE l.personnummer = :fnr
        AND v.saksblokk = :saksblokk
        AND v.saksnummer = :saksnummer
    """,
    )
    fun hentVedtak(
        fnr: String,
        saksnummer: Long,
        saksblokk: String,
    ): List<Vedtak>

    /**
     * JPQL. Teller vedtak på barnetrygd som venter på beslutning for personen [fnr].
     *
     * Et vedtak telles når:
     * - stønaden er på rutine `BA`
     * - vedtaket ikke har resultatkode `HB`
     * - endringskoden ikke er `UA` eller `AN`
     * - vedtaket ikke er godkjent av beslutter (ingen Beslutning med `godkjent2 = 'J'`)
     * - vedtaket har minst én delytelse
     *
     * Brukes for å avgjøre om personen har en åpen sak til beslutning.
     */
    @Query(
        """
        SELECT count(*) FROM LøpeNrFnr l, StønadDb2 s, Vedtak v, Endring e
        WHERE l.personnummer = :fnr
        AND s.løpenummer = l.personLøpenummer
        AND s.kodeRutine = 'BA'
        AND v.løpenummer = s.løpenummer
        AND v.stønadId = s.stønadId
        AND v.kodeRutine = s.kodeRutine
        AND v.kodeResultat <> 'HB'
        AND e.vedtakId = v.vedtakId
        AND e.kode <> 'UA'
        AND e.kode <> 'AN'
        AND NOT EXISTS (SELECT 1 FROM Beslutning b
                        WHERE b.vedtakId = v.vedtakId
                        AND b.godkjent2 = 'J')
        AND EXISTS (SELECT 1 FROM Delytelse d
                    WHERE d.id.vedtakId = v.vedtakId)
    """,
    )
    fun tellAntallÅpneSakerPåPerson(fnr: String): Long

    /**
     * Native SQL. Henter stønadsklassifiseringen (T_STONADSKLASSE) for alle barnetrygdvedtak
     * (rutine `BA`) på saken med [tkNr], [saksblokk] og [saksnummer] for personen [fnr].
     *
     * Hver rad er ett nivå i klassifiseringen av ett vedtak. Nivå `02` gir valget
     * (f.eks. `OR` eller `UT`) og nivå `03` gir undervalget (f.eks. `MD`).
     * `{h-schema}` erstattes av Hibernate med standardskjemaet.
     */
    @Query(
        """
        SELECT s.vedtak_id vedtakId, s.kode_nivaa kodeNivaa, s.kode_klasse kodeKlasse FROM {h-schema}T_STONADSKLASSE s
        WHERE s.VEDTAK_ID IN (
            SELECT v.vedtak_id FROM {h-schema}T_VEDTAK v
                INNER JOIN {h-schema}T_LOPENR_FNR l
                ON v.PERSON_LOPENR = l.PERSON_LOPENR
            WHERE l.PERSONNR = :fnr
            AND v.KODE_RUTINE = 'BA'
            AND v.TKNR = :tkNr
            AND v.SAKSBLOKK = :saksblokk
            AND v.SAKSNR = :saksnummer
        )""",
        nativeQuery = true,
    )
    fun hentStønadsklassifisering(
        fnr: String,
        tkNr: String,
        saksblokk: String,
        saksnummer: Long,
    ): List<Stønadsklasse>
}

interface Stønadsklasse {
    val vedtakId: Long
    val kodeNivaa: String
    val kodeKlasse: String
}

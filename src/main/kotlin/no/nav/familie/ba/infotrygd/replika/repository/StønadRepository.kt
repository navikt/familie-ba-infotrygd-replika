package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.commons.foedselsnummer.FoedselsNr
import no.nav.familie.ba.infotrygd.replika.model.dl1.Sak
import no.nav.familie.ba.infotrygd.replika.model.dl1.Stønad
import no.nav.familie.ba.infotrygd.replika.model.dl1.TrunkertStønad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Datoformater brukt i spørringene:
 * - `opphørtFom` og `utbetalingTom` er på formatet `MMyyyy`. `'000000'` betyr ingen sluttdato (løpende).
 *   `substring(x, 3, 4)` henter ut årstallet.
 * - `virkningFom` lagres invertert (`9999 - år` i de fire første sifrene). Derfor betyr
 *   `substring(virkningFom, 1, 4) >= 9999 - år` at virkningsdatoen er i eller før [år].
 */
@Repository
interface StønadRepository : JpaRepository<Stønad, Long> {
    /**
     * JPQL. Henter alle stønader (løpende og historiske) til personene i [fnr].
     * Stønaden kobles til personen på personnøkkel og region.
     */
    @Query(
        """
        SELECT s FROM Stønad s
           INNER JOIN Person p
                   ON (s.personKey = p.personKey and
                       s.region = p.region)
        WHERE p.fnr IN :fnr
    """,
    )
    fun findStønadByFnr(fnr: List<FoedselsNr>): List<Stønad>

    /**
     * JPQL. Henter stønader for personen [fnr] som har barn og som har månedlige utbetalinger
     * som løper i eller etter [år].
     *
     * Krav:
     * - stønaden har minst ett barn (`antallBarn > 0`)
     * - det finnes en månedlig utbetaling (type `M`) for samme stønad (personnøkkel, iverksattFom og
     *   virkningFom) som enten er løpende (`utbetalingTom = '000000'`) eller slutter i eller etter [år]
     *
     * Returnerer en [TrunkertStønad] med personens pensjonstrygdet-flagg.
     */
    @Query(
        """SELECT new no.nav.familie.ba.infotrygd.replika.model.dl1.TrunkertStønad(s.id, s.personKey, s.fnr, s.sakNr, s.saksblokk, s.status, s.region, s.virkningFom, s.opphørtFom, s.iverksattFom, s.antallBarn, p.pensjonstrygdet) FROM Stønad s
           INNER JOIN Person p
                   ON (s.personKey = p.personKey and
                       s.region = p.region)
        WHERE p.fnr = :fnr
        AND s.antallBarn > 0
        AND EXISTS (SELECT u FROM Utbetaling  u
            WHERE u.personKey = s.personKey   
            AND u.startUtbetalingMåned = s.iverksattFom
            AND u.virksomFom = s.virkningFom
            AND u.utbetalingstype = 'M'
            AND (u.utbetalingTom = '000000' or CAST(substring(u.utbetalingTom, 3, 4) as integer) >= :år))
    """,
    )
    fun findTrunkertStønadMedUtbetalingÅrByFnr(
        fnr: FoedselsNr,
        år: Int,
    ): List<TrunkertStønad>

    /**
     * JPQL. Henter alle stønader (uavhengig av person) som var aktive i [år] og har månedlige
     * utbetalinger i eller etter [år]. Brukes til å finne alle personer med barnetrygd i et gitt år.
     *
     * Krav:
     * - stønaden er løpende eller opphørte i eller etter [år]
     * - virkningsdatoen er i eller før [år] (se inverteringen i klassedokumentasjonen)
     * - status er en av [statusKoder]
     * - stønaden har minst ett barn
     * - det finnes en månedlig utbetaling (type `M`) for stønaden som er løpende eller slutter i
     *   eller etter [år]
     *
     * Returnerer [TrunkertStønad] uten pensjonstrygdet-flagg (tom streng), siden Person ikke er koblet inn.
     */
    @Query(
        """SELECT new no.nav.familie.ba.infotrygd.replika.model.dl1.TrunkertStønad(s.id, s.personKey, s.fnr, s.sakNr, s.saksblokk, s.status, s.region, s.virkningFom, s.opphørtFom, s.iverksattFom, s.antallBarn, '')
        FROM Stønad s
        WHERE (s.opphørtFom='000000' or CAST(substring(s.opphørtFom, 3, 4) as integer) >= :år)
        AND CAST(substring(s.virkningFom, 1, 4) as integer) >= (9999 - :år)
        AND s.status in :statusKoder
        AND s.antallBarn > 0
        AND EXISTS (SELECT u FROM Utbetaling  u
                    WHERE u.personKey = s.personKey   
                    AND u.startUtbetalingMåned = s.iverksattFom
                    AND u.virksomFom = s.virkningFom
                    AND u.utbetalingstype = 'M'
                    AND (u.utbetalingTom = '000000' or CAST(substring(u.utbetalingTom, 3, 4) as integer) >= :år))""",
    )
    fun findStønadMedUtbetalingByÅrAndStatusKoder(
        år: Int,
        vararg statusKoder: String,
    ): List<TrunkertStønad>

    /**
     * JPQL. Henter stønadene til personen [fnr] som var aktive i [år].
     *
     * Krav:
     * - stønaden er løpende eller opphørte i eller etter [år]
     * - virkningsdatoen er i eller før [år]
     * - status er en av [statusKoder]
     * - stønaden har minst ett barn
     *
     * Sjekker ikke utbetalinger, i motsetning til [findTrunkertStønadMedUtbetalingÅrByFnr].
     */
    @Query(
        """SELECT s FROM Stønad s
                INNER JOIN Person p
                ON (s.personKey = p.personKey and
                        s.region = p.region)
                WHERE p.fnr = :fnr
                AND (s.opphørtFom='000000' or CAST(substring(s.opphørtFom, 3, 4) as integer) >= :år)
                AND CAST(substring(s.virkningFom, 1, 4) as integer) >= (9999 - :år)
                AND s.status in :statusKoder
                AND s.antallBarn > 0""",
    )
    fun findStønadByÅrAndStatusKoderAndFnr(
        fnr: FoedselsNr,
        år: Int,
        vararg statusKoder: String,
    ): List<Stønad>

    /**
     * JPQL. Henter løpende stønader (ikke opphørt, `opphørtFom = '000000'`) til personene i [fnr].
     */
    @Query(
        """
        SELECT s FROM Stønad s
           INNER JOIN Person p
                   ON (s.personKey = p.personKey and
                       s.region = p.region)
        WHERE p.fnr IN :fnr
        AND s.opphørtFom = '000000'
    """,
    )
    fun findLøpendeStønadByFnr(fnr: List<FoedselsNr>): List<Stønad>

    /**
     * JPQL. Henter alle stønader (løpende og historiske) der minst ett av barna i [barnFnr] er registrert.
     * Stønaden kobles til barnet på personnøkkel og region.
     */
    @Query(
        """
        SELECT s FROM Stønad s
           INNER JOIN Barn b
                   ON (s.personKey = b.personKey and
                       s.region = b.region)
        WHERE b.barnFnr IN :barnFnr
    """,
    )
    fun findStønadByBarnFnr(barnFnr: List<FoedselsNr>): List<Stønad>

    /**
     * JPQL. Henter løpende stønader der minst ett av barna i [barnFnr] har løpende barnetrygd.
     * Både barnet (`barnetrygdTom = '000000'`) og stønaden (`opphørtFom = '000000'`) må være uten sluttdato.
     */
    @Query(
        """
        SELECT s FROM Stønad s
           INNER JOIN Barn b
                   ON (s.personKey = b.personKey and
                       s.region = b.region)
        WHERE b.barnFnr IN :barnFnr
        AND b.barnetrygdTom = '000000'
        AND s.opphørtFom = '000000'
    """,
    )
    fun findLøpendeStønadByBarnFnr(barnFnr: List<FoedselsNr>): List<Stønad>

    /**
     * JPQL. Henter stønadene som hører til [sak].
     * Kobles på personnøkkel, saksblokk, saksnummer og region.
     */
    @Query(
        """
        SELECT s FROM Stønad s
        WHERE s.personKey = :#{#sak.personKey}
        AND s.saksblokk = :#{#sak.saksblokk}
        AND s.sakNr = :#{#sak.saksnummer}
        AND s.region = :#{#sak.region}
    """,
    )
    fun findStønadBySak(sak: Sak): List<Stønad>

    /**
     * JPQL. Henter minste `iverksattFom` blant stønadene til [personKey].
     * Datoen lagres invertert, så minste verdi tilsvarer seneste iverksettingsdato.
     */
    @Query(
        """
        SELECT MIN(s.iverksattFom) FROM Stønad s
        WHERE s.personKey = :personKey
    """,
    )
    fun findSenesteIverksattFomByPersonKey(personKey: Long): String

    /**
     * JPQL. Henter én bestemt stønad, identifisert av personnøkkel, iverksettingsdato,
     * virkningsdato og region. Feiler hvis det finnes flere treff.
     */
    @Query(
        """
        SELECT s FROM Stønad s
        WHERE s.personKey = :personKey
        AND s.iverksattFom = :iverksattFom
        AND s.virkningFom = :virkningFom
        AND s.region = :region
    """,
    )
    fun findStønad(
        personKey: Long,
        iverksattFom: String,
        virkningFom: String,
        region: String,
    ): Stønad
}

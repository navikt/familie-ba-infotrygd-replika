package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.commons.foedselsnummer.FoedselsNr
import no.nav.familie.ba.infotrygd.replika.model.dl1.Sak
import no.nav.familie.ba.infotrygd.replika.model.dl1.TrunkertStønad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Sakstyper brukt i spørringene: S = søknad, R = revurdering, K = klage, A = anke,
 * FL = flyttesak, AS = automatisk stønad. For FS og øvrige koder, se Confluence-siden om sakskoder i README.
 * Valg: OR = ordinær barnetrygd, UT = utvidet barnetrygd.
 */
@Repository
interface SakRepository : JpaRepository<Sak, Long> {
    /**
     * JPQL. Henter barnetrygdsaker (kapittel `BA`) der stønadsmottaker har fødselsnummer [fnr].
     * Tar med sakstypene S, R, FL, AS og FS. Klage (K) og anke (A) er ikke med.
     */
    @Query(
        """
        SELECT s FROM Sak s
            WHERE s.fnr = :fnr 
              AND s.kapittelNr = 'BA' 
              AND s.type IN ('S', 'R', 'FL', 'AS', 'FS')""",
    ) // søknad, revurdering, klage, anke, flyttesak, automatisk stønad,
    fun findBarnetrygdsakerByFnr(fnr: FoedselsNr): List<Sak>

    /**
     * JPQL. Henter barnetrygdsaker (kapittel `BA`) til stønadsmottakere som har minst ett av barna i
     * [barnFnr] registrert. Saken kobles til barnet på personnøkkel og region.
     * Tar med sakstypene S, R, K, A, FL og AS.
     */
    @Query(
        """
        SELECT sak FROM Sak sak
           INNER JOIN Barn barn 
                   ON (sak.personKey = barn.personKey AND 
                       sak.region = barn.region)
           WHERE barn.barnFnr IN :barnFnr
             AND sak.kapittelNr = 'BA' 
             AND sak.type IN ('S', 'R', 'K', 'A', 'FL', 'AS')""",
    ) // søknad, revurdering, klage, anke, flyttesak, automatisk stønad
    fun findBarnetrygdsakerByBarnFnr(barnFnr: List<FoedselsNr>): List<Sak>

    /**
     * JPQL. Henter sakene om utvidet barnetrygd (valg `UT`) som hører til [stonad].
     * Saken kobles til stønaden på personnøkkel, saksblokk, saksnummer og region.
     * Tar med sakstypene S, R, K, A, FL og AS.
     */
    @Query(
        """
        SELECT s FROM Sak s 
            WHERE s.personKey = :#{#stonad.personKey}
              AND s.kapittelNr = 'BA'
              AND s.valg = 'UT'
              AND s.saksblokk = :#{#stonad.saksblokk}
              AND s.saksnummer = :#{#stonad.sakNr}
              AND s.region = :#{#stonad.region}
              AND s.type IN ('S', 'R', 'K', 'A', 'FL', 'AS')""",
    )
    fun hentUtvidetBarnetrygdsakerForStønad(stonad: TrunkertStønad): List<Sak>

    /**
     * JPQL. Som [hentUtvidetBarnetrygdsakerForStønad], men tar med både ordinær (`OR`)
     * og utvidet (`UT`) barnetrygd.
     */
    @Query(
        """
        SELECT s FROM Sak s 
            WHERE s.personKey = :#{#stonad.personKey}
              AND s.kapittelNr = 'BA'
              AND s.valg IN ('OR','UT')
              AND s.saksblokk = :#{#stonad.saksblokk}
              AND s.saksnummer = :#{#stonad.sakNr}
              AND s.region = :#{#stonad.region}
              AND s.type IN ('S', 'R', 'K', 'A', 'FL', 'AS')""",
    )
    fun hentBarnetrygdsakerForStønad(stonad: TrunkertStønad): List<Sak>
}

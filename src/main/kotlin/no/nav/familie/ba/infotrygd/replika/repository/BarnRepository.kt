package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.commons.foedselsnummer.FoedselsNr
import no.nav.familie.ba.infotrygd.replika.model.dl1.Barn
import no.nav.familie.ba.infotrygd.replika.model.dl1.TrunkertStønad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BarnRepository : JpaRepository<Barn, Long> {
    /**
     * JPQL. Henter alle barnerader der barnets fødselsnummer er i [barnFnrList].
     * Ingen filtrering på periode, så både løpende og avsluttede barn returneres.
     */
    @Query(
        """
        SELECT b FROM Barn b
        WHERE b.barnFnr IN :barnFnrList
    """,
    )
    fun findBarnByFnrList(barnFnrList: List<FoedselsNr>): List<Barn>

    /**
     * JPQL. Henter barna som hører til en bestemt stønad. Barn knyttes til stønaden på
     * personnøkkel, iverksettingsdato, virkningsdato og region.
     */
    @Query(
        """
        SELECT b FROM Barn b
        WHERE b.personKey = :#{#stonad.personKey}
        AND b.iverksatt = :#{#stonad.iverksattFom}
        AND b.virkningFom = :#{#stonad.virkningFom}
        AND b.region = :#{#stonad.region}
    """,
    )
    fun findBarnByStønad(stonad: TrunkertStønad): List<Barn>

    /**
     * JPQL. Henter barn registrert på stønadsmottakeren med [personKey].
     * Uten [historikk] returneres bare barn med løpende barnetrygd (`barnetrygdTom = '000000'`,
     * dvs. ingen sluttdato). Med `historikk = true` returneres alle barn.
     */
    @Query(
        """
        SELECT b FROM Barn b
        WHERE b.personKey = :personKey
        AND (b.barnetrygdTom = '000000' OR :historikk = true)
    """,
    )
    fun findBarnByPersonkey(
        personKey: Long,
        historikk: Boolean = false,
    ): List<Barn>
}

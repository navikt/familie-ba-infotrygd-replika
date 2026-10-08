package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.commons.foedselsnummer.FoedselsNr
import no.nav.familie.ba.infotrygd.replika.model.dl1.Hendelse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface HendelseRepository : JpaRepository<Hendelse, Long> {
    /**
     * JPQL. Henter hendelser (f.eks. sendte brev) for personene i [fnr] med tekstkode i [tekstKoder],
     * som skjedde på eller etter en gitt dato.
     *
     * Aksjonsdatoen lagres invertert som `99999999 - yyyyMMdd`. Derfor betyr
     * `aksjonsdatoSeq <= :aksjonsdatoSeq` at hendelsen er på eller etter datoen.
     * Eksempel: alle brev etter 2021-12-01 gir (99999999 - 20211201) = 79788798.
     *
     * Brevkoder (`tekstKode1`) som ba-sak har brukt. Infotrygd lagrer den korte koden:
     * - `BA04`: batch, opphør småbarnstillegg
     * - `BA05`: batch, innvilget småbarnstillegg
     * - `BA37`: batch, omregning barn 18 år
     * - `BA18`: batch, omregning barn 6 år
     * - `B001`: manuell, opphør småbarnstillegg
     * - `B002`: manuell, innvilget småbarnstillegg
     * - `B003`: manuell, omregning barn 18 år
     * - `BA19`: manuell, omregning barn 6 år
     */
    @Query(
        """
        SELECT h FROM Hendelse h
        WHERE h.fnr in :fnr
        AND h.tekstKode1 in :tekstKoder
        AND h.aksjonsdatoSeq <= :aksjonsdatoSeq
    """,
    )
    fun findHendelseByFnrInAndTekstKoderIn(
        fnr: List<FoedselsNr>,
        tekstKoder: List<String>,
        aksjonsdatoSeq: Long,
    ): List<Hendelse>
}

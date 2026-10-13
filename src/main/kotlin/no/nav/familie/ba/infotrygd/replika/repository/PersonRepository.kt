package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.familie.ba.infotrygd.replika.model.dl1.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long> {
    /**
     * JPQL. Henter mottakernummeret til personen med [personKey].
     * Returnerer `null` hvis personen ikke finnes eller mangler mottakernummer.
     */
    @Query(
        """
        SELECT p.mottakerNummer FROM Person p
        WHERE p.personKey = :personKey
    """,
    )
    fun findMottakerNummerByPersonkey(personKey: Long): Long?
}

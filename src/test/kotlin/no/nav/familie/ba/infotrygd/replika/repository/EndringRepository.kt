package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.familie.ba.infotrygd.replika.model.db2.Endring
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EndringRepository : JpaRepository<Endring, Long>

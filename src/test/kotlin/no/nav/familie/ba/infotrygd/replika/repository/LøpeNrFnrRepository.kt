package no.nav.familie.ba.infotrygd.replika.repository

import no.nav.familie.ba.infotrygd.replika.model.db2.LøpeNrFnr
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LøpeNrFnrRepository : JpaRepository<LøpeNrFnr, Long>

package no.nav.familie.ba.infotrygd.replika.rest.controller

import no.nav.familie.ba.infotrygd.replika.normalisering.FnrKolonne
import no.nav.familie.ba.infotrygd.replika.normalisering.FnrNormaliseringRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class FnrNormaliseringRespons(
    val antallOppdatert: Int,
)

@PreAuthorize("hasRole('FORVALTER')")
@RestController
@RequestMapping("/api/forvalter/fnr-normalisering")
class FnrNormaliseringController(
    private val repository: FnrNormaliseringRepository,
) {
    @PostMapping("/{kolonne}")
    @Transactional
    fun normaliser(
        @PathVariable kolonne: FnrKolonne,
    ): FnrNormaliseringRespons = FnrNormaliseringRespons(repository.normaliser(kolonne))
}

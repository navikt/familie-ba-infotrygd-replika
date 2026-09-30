package no.nav.familie.ba.infotrygd.replika.service
import no.nav.familie.ba.infotrygd.replika.Profiles.NOAUTH
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile(NOAUTH)
class TilgangskontrollService {
    fun sjekkTilgang() {
    }
}

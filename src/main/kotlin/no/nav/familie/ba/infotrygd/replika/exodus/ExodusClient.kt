package no.nav.familie.ba.infotrygd.replika.exodus

import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URI

/**
 * Klient mot familie-ba-exodus sitt REST-API. /api/tellRader er kun ment til engangsbruk for
 * progresjonsvisning, og skal ikke kalles fra replikeringsløypa (se ExodusKlientService).
 */
@Component
class ExodusClient(
    private val exodusRestClient: RestClient,
    exodusProperties: ExodusProperties,
) {
    private val baseUrl = exodusProperties.baseUrl.trimEnd('/')

    fun hentUttrekk(
        tabell: ExodusTabell,
        iterator: String?,
        antallRader: Int,
    ): HentUttrekkResponse =
        try {
            exodusRestClient
                .post()
                .uri(URI.create("$baseUrl/api/hentUttrekk"))
                .body(HentUttrekkRequest(tabell.tabellNavn, iterator, antallRader.toLong()))
                .retrieve()
                .body<HentUttrekkResponse>() ?: HentUttrekkResponse(iterator = iterator.orEmpty())
        } catch (e: HttpClientErrorException.Conflict) {
            throw NyBaselineException(tabell)
        }

    fun tellRader(tabell: ExodusTabell): Long =
        exodusRestClient
            .post()
            .uri(URI.create("$baseUrl/api/tellRader"))
            .body(TellRaderRequest(tabell.tabellNavn))
            .retrieve()
            .body<TellRaderResponse>()
            ?.antall ?: 0
}

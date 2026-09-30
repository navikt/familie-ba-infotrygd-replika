package no.nav.familie.ba.infotrygd.replika.config

import no.nav.familie.felles.tokenklient.entraid.EntraIDClient
import no.nav.familie.felles.tokenklient.entraid.EntraIDRestClientFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestClient

@Configuration
@Import(
    EntraIDRestClientFactory::class,
    EntraIDClient::class,
)
class RestClientConfig(
    private val entraIDRestClientFactory: EntraIDRestClientFactory,
) {
    @Bean("exodusRestClient")
    fun exodusRestClient(
        @Value("\${EXODUS_SCOPE}") scope: String,
    ): RestClient = entraIDRestClientFactory.lagMaskinTilMaskinRestKlient(scope)
}

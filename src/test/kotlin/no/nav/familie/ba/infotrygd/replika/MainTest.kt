package no.nav.familie.ba.infotrygd.replika

import no.nav.familie.ba.infotrygd.replika.testutil.MockOAuth2ServerInitializer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = [MockOAuth2ServerInitializer::class])
class MainTest {
    @Test
    fun contextLoads() {
    }
}

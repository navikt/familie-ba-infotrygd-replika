package no.nav.familie.ba.infotrygd.replika

import no.nav.familie.ba.infotrygd.replika.exodus.ExodusProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ExodusProperties::class)
class Main

fun main(args: Array<String>) {
    System.setProperty("oracle.jdbc.fanEnabled", "false")
    runApplication<Main>(*args)
}

package unijore

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import kotlin.jvm.java

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@SpringBootApplication
class Application {
    private val log = LoggerFactory.getLogger(Application::class.java)

    @Bean
    fun init() = CommandLineRunner {
        log.info("Hello World!")
    }
}

fun main() {
    runApplication<Application>()
}

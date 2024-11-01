package ujodsri

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
class DataBoundaryTest(@Autowired private val restTemplate:TestRestTemplate) {

    @Test
    fun data_returns_data() {
        val actual = restTemplate.getForObject<String>("/data")
        assertEquals("""{"value": "dummy"}""", actual)
    }
}

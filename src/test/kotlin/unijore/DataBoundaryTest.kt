package unijore

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration


@SpringBootTest(
    webEnvironment = WebEnvironment.DEFINED_PORT,
    properties = ["server.port=8880", "spring.main.allow-bean-definition-overriding=true"]
)
@ContextConfiguration(classes = [TestApplicationConfiguration::class])
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class DataBoundaryTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun `get data when there is no data returns empty list`() {
        val actual: List<Any>? = restTemplate.getForObject<List<Any>>("/data")

        assertThat(actual).isEmpty()
    }

    @Test
    fun `post data returns success response`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)

        val actual: ResponseEntity<String> = restTemplate.postForEntity("/data", entity, String::class.java)

        assertThat(actual.statusCode.value()).isEqualTo(200)
        assertThat(actual.body!!).isNotEmpty
    }

    @Test
    fun `get returns posted data`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)
        restTemplate.postForEntity("/data", entity, Long::class.java)

        val actual: JsonArray? = restTemplate.getForObject<JsonArray>("/data")

        assertThat(actual).hasSize(1).satisfiesExactly(
            { element ->
                assertThat(element)
                    .isInstanceOfSatisfying(JsonObject::class.java, {
                        o: JsonObject? -> assertThat(o).containsEntry("test_key", JsonPrimitive("dummy_value")) })
            }
        )
    }
}

package unijore

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
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
@TestMethodOrder(MethodOrderer.MethodName::class)
class DataBoundaryTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun `get ids returns empty list when there is no data`() {
        val responseData: List<Any>? = restTemplate.getForObject<List<Any>>("/ids")

        assertThat(responseData).isEmpty()
    }

    @Test
    fun `get ids returns id of posted data`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)
        val postedId = restTemplate.postForObject("/data", entity, String::class.java)

        val responseData: List<String>? = restTemplate.getForObject("/ids")

        assertThat(responseData).containsExactly(postedId)
    }

    @Test
    fun `get all data when there is no data returns empty list`() {
        val responseData: List<Any>? = restTemplate.getForObject("/data")

        assertThat(responseData).isEmpty()
    }

    @Test
    fun `get all data returns posted data`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)
        restTemplate.postForEntity("/data", entity, String::class.java)

        val responseData: JsonArray? = restTemplate.getForObject("/data")

        assertThat(responseData).hasSize(1).satisfiesExactly(
            { element ->
                assertThat(element)
                    .isInstanceOfSatisfying(
                        JsonObject::class.java,
                        { o: JsonObject? -> assertThat(o).containsEntry("test_key", JsonPrimitive("dummy_value")) })
            }
        )
    }

    @Test
    fun `get data for id when there is no data returns '404 NOT FOUND'`() {
        val responseEntity = restTemplate.getForEntity("/data/42", Any::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `get data for id returns posted data`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)
        val id = restTemplate.postForObject("/data", entity, String::class.java)

        val responseData: JsonObject? = restTemplate.getForObject("/data/$id")

        assertThat(responseData).containsEntry("test_key", JsonPrimitive("dummy_value"))
    }

    @Test
    fun `post data returns success response`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity("/data", entity, String::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body!!).isNotEmpty
    }

    @Test
    fun `000-delete data when there is no data returns 'NOT FOUND'`() { // execute fist because of Spring Boot bug
        val responseEntity: ResponseEntity<Unit> =
            restTemplate.exchange("/data/42", HttpMethod.DELETE, HttpEntity.EMPTY, Unit::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `delete data when deleting data does return OK`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)
        val id = restTemplate.postForObject("/data", entity, String::class.java)

        val responseEntity: ResponseEntity<Unit> =
            restTemplate.exchange("/data/$id", HttpMethod.DELETE, HttpEntity.EMPTY, Unit::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `get data for id when data is deleted returns '404 NOT FOUND'`() {
        val headers = HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = HttpEntity("""{ "test_key" : "dummy_value" }""", headers)
        val id = restTemplate.postForObject("/data", entity, String::class.java)

        restTemplate.delete("/data/$id")

        val getResponseEntity = restTemplate.getForEntity("/data/$id", Any::class.java)
        assertThat(getResponseEntity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}

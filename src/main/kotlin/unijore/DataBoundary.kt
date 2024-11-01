package unijore

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@RestController
class DataBoundary(private val dataRepository: DataRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/data")
    fun retrieve(): Iterable<DataObject> = dataRepository.findAll();

    @PostMapping("/data")
    // TODO or (@RequestBody: payload: JsonNode)?
    fun store(@RequestBody payload: Map<String, Object>): ResponseEntity<Long> {
        logger.info("Got data: $payload")
        val d: DataObject = DataObject(data = payload.toString())
        val savedData = dataRepository.save(d)
        return ResponseEntity(savedData.id, HttpStatus.OK)
    }
}

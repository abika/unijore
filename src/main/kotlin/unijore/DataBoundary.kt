package unijore

import org.dizitart.no2.collection.Document
import org.dizitart.no2.collection.NitriteCollection
import org.dizitart.no2.collection.NitriteId
import org.dizitart.no2.common.WriteResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@RestController
class DataBoundary(private val dataCollection: NitriteCollection) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/ids")
    fun retrieveIds(): List<String> {
        return dataCollection.find().map { d -> d.id.idValue }
    };

    @GetMapping("/data")
    fun retrieve(): List<Document> {
        return dataCollection.find().toList()
    };

    @GetMapping("/data/{id}")
    fun retrieve(@PathVariable id: String): ResponseEntity<Document> {
        val document = dataCollection.getById(NitriteId.createId(id))
        return if (document != null)
            ResponseEntity<Document>(document, HttpStatus.OK)
        else
            ResponseEntity.notFound().build()
    }

    @PostMapping("/data")
    // TODO or (@RequestBody: payload: JsonNode)?
    fun store(@RequestBody payload: Map<String, Object>): ResponseEntity<String> {
        logger.info("Got data: $payload")

        val document: Document = Document.createDocument(payload)
        val writeResult: WriteResult = dataCollection.insert(document)
        val documentId: NitriteId = writeResult.first()

        return ResponseEntity<String>(documentId.idValue, HttpStatus.OK)
    }
}

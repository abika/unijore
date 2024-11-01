package unijore

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@RestController
class DataBoundary {

    @GetMapping("/data")
    fun findAll(): String {
        return if (true) """{"value": "dummy"}""" else ""
    }
}

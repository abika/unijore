package unijore

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.util.Date

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@Entity
data class DataObject(@Id @GeneratedValue val id: Long? = null,
                      @Temporal(TemporalType.TIMESTAMP) val created: Date = Date(),
                      @Temporal(TemporalType.TIMESTAMP) val modified: Date = Date(),
                      val data: String)

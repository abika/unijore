package unijore

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@Entity
data class DataObject(@Id @GeneratedValue val id: Long? = null, val data: String)

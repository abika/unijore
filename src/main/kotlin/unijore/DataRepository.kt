package unijore

import org.springframework.data.repository.CrudRepository

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
interface DataRepository : CrudRepository<DataObject, Long>

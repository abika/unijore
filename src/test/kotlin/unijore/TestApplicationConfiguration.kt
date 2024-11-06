package unijore

import org.dizitart.no2.Nitrite
import org.dizitart.no2.collection.NitriteCollection
import org.dizitart.no2.mvstore.MVStoreModule
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@TestConfiguration
class TestApplicationConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun database(): NitriteCollection {
        val storeModule: MVStoreModule = MVStoreModule.withConfig()
            .build();

        val db: Nitrite = Nitrite.builder()
            .loadModule(storeModule)
            .openOrCreate();

        return db.getCollection("data")
    }
}

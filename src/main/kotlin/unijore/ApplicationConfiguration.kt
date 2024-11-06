package unijore

import org.dizitart.no2.Nitrite
import org.dizitart.no2.collection.NitriteCollection
import org.dizitart.no2.mvstore.MVStoreModule
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

/**
 * @author Alexander Bikadorov {@literal <goto@openmailbox.org>}
 */
@Configuration
class ApplicationConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun database(): NitriteCollection {
        val storeModule: MVStoreModule = MVStoreModule.withConfig()
            .filePath("data.db")
            .build();

        val db: Nitrite = Nitrite.builder()
            .loadModule(storeModule)
            .openOrCreate();

        return db.getCollection("data")
    }
}

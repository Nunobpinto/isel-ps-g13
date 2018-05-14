package isel.leic.ps.eduWikiAPI.configuration

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class PersistenceConfiguration {

    @Bean
    fun dbiBean(dataSource: DataSource) : Jdbi? =
            Jdbi.create(dataSource)
                    .installPlugin(KotlinPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())
}
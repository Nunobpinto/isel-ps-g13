package isel.leic.ps.eduWikiAPI.configuration

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.annotation.RequestScope
import javax.sql.DataSource

@Configuration
class PersistenceConfiguration {

    @Bean
    fun jdbiBean(dataSource: DataSource): Jdbi =
            Jdbi.create(dataSource)
                    // Plugins
                    .installPlugin(KotlinPlugin())
                    .installPlugin(PostgresPlugin())

    @Bean(destroyMethod = "close")
    @RequestScope
    fun jdbiHandleBean(jdbi: Jdbi): Handle {
        val handle = jdbi.open()
        return handle
    }
}
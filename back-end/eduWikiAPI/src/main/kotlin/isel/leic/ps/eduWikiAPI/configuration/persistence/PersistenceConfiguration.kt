package isel.leic.ps.eduWikiAPI.configuration.persistence

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.transaction.PlatformTransactionManager
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource

@Configuration
class PersistenceConfiguration {

    // ----------------------------
    // Main database configuration
    // ----------------------------

    @Bean
    @ConfigurationProperties("spring.datasource")
    fun mainDatabase(): HikariDataSource =
            DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean
    fun transactionAwareMainDataSourceProxy(dataSource: HikariDataSource): TransactionAwareDataSourceProxy =
            TransactionAwareDataSourceProxy(dataSource)

    @Bean
    fun platformMainTransactionManager(dataSource: HikariDataSource): PlatformTransactionManager =
            DataSourceTransactionManager(dataSource)

    @Bean
    fun jdbiMainDatabaseBean(transactionAwareDataSourceProxy: TransactionAwareDataSourceProxy): Jdbi =
            Jdbi.create(transactionAwareDataSourceProxy)
                    // Plugins
                    .installPlugin(KotlinPlugin())
                    .installPlugin(PostgresPlugin())
                    .installPlugin(SqlObjectPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())
                    .setTemplateEngine(SchemaReWriter())
}
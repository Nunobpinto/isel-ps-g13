package isel.leic.ps.eduWikiAPI.configuration

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

    @Bean(name = ["mainDatabase"])
    @Primary
    @ConfigurationProperties("main-database.datasource")
    fun mainDatabase(): HikariDataSource =
            DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["transactionAwareMainDataSourceProxy"])
    fun transactionAwareMainDataSourceProxy(@Qualifier("mainDatabase") dataSource: HikariDataSource): TransactionAwareDataSourceProxy =
            TransactionAwareDataSourceProxy(dataSource)

    @Bean(name = ["platformMainTransactionManager"])
    @Primary
    fun platformMainTransactionManager(@Qualifier("mainDatabase") dataSource: HikariDataSource): PlatformTransactionManager =
            DataSourceTransactionManager(dataSource)

    @Bean(name = ["MainJdbi"])
    @Primary
    fun jdbiMainDatabaseBean(@Qualifier("transactionAwareMainDataSourceProxy") transactionAwareDataSourceProxy: TransactionAwareDataSourceProxy): Jdbi =
            Jdbi.create(transactionAwareDataSourceProxy)
                    // Plugins
                    .installPlugin(KotlinPlugin())
                    .installPlugin(PostgresPlugin())
                    .installPlugin(SqlObjectPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())

    // ----------------------------
    // Resources database configuration
    // ----------------------------

    @Bean(name = ["resourcesDatabase"])
    @ConfigurationProperties("resources-database.datasource")
    fun resourcesDatabase(): DataSource =
            DataSourceBuilder.create().build()

    @Bean(name = ["transactionAwareResourcesDataSourceProxy"])
    fun transactionAwareResourcesDataSourceProxy(@Qualifier("resourcesDatabase") dataSource: DataSource): TransactionAwareDataSourceProxy =
            TransactionAwareDataSourceProxy(dataSource)

    @Bean(name = ["platformResourcesTransactionManager"])
    fun platformResourcesTransactionManager(@Qualifier("resourcesDatabase") dataSource: DataSource): PlatformTransactionManager =
            DataSourceTransactionManager(dataSource)

    @Bean(name = ["ResourcesJdbi"])
    fun jdbiResourcesDatabaseBean(@Qualifier("transactionAwareResourcesDataSourceProxy") transactionAwareDataSourceProxy: TransactionAwareDataSourceProxy): Jdbi =
            Jdbi.create(transactionAwareDataSourceProxy)
                    // Plugins
                    .installPlugin(KotlinPlugin())
                    .installPlugin(PostgresPlugin())
                    .installPlugin(SqlObjectPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())
}
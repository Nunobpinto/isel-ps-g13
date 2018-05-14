package isel.leic.ps.eduWikiAPI.configuration

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.mappers.OrganizationReportRowMapper
import isel.leic.ps.eduWikiAPI.repository.mappers.OrganizationRowMapper
import isel.leic.ps.eduWikiAPI.repository.mappers.OrganizationVersionRowMapper
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
                    // Plugins
                    .installPlugin(KotlinPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())
                    // Mappers
                    .registerRowMapper(Organization::class.java, OrganizationRowMapper())
                    .registerRowMapper(OrganizationVersion::class.java, OrganizationVersionRowMapper())
                    .registerRowMapper(OrganizationReport::class.java, OrganizationReportRowMapper())
}
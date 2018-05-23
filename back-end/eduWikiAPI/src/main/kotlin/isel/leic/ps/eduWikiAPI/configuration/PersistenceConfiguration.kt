package isel.leic.ps.eduWikiAPI.configuration

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.mappers.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class PersistenceConfiguration {

    @Bean
    fun jdbiBean(dataSource: DataSource): Jdbi? =
            Jdbi.create(dataSource)
                    // Plugins
                    .installPlugin(KotlinPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())
                    // Mappers
                    .registerRowMapper(Organization::class.java, OrganizationRowMapper())
                    .registerRowMapper(OrganizationVersion::class.java, OrganizationVersionRowMapper())
                    .registerRowMapper(OrganizationReport::class.java, OrganizationReportRowMapper())
                    .registerRowMapper(Programme::class.java, ProgrammeRowMapper())
                    .registerRowMapper(ProgrammeVersion::class.java, ProgrammeVersionRowMapper())
                    .registerRowMapper(ProgrammeReport::class.java, ProgrammeReportRowMapper())
                    .registerRowMapper(ProgrammeStage::class.java, ProgrammeStageRowMapper())
}
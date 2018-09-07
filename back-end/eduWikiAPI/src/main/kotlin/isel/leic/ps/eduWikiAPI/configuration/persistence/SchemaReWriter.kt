package isel.leic.ps.eduWikiAPI.configuration.persistence

import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.core.statement.TemplateEngine
import java.util.Objects.nonNull

/**
 * Simple class that substitutes :schema for the schema caught in the current http request
 * Checks for existence of ":schema"
 */
class SchemaReWriter : TemplateEngine {

    override fun render(template: String, ctx: StatementContext?): String {
        var sql = template
        if( nonNull(TenantContext.getTenantSchema()) )
            sql = sql.replace(":schema".toRegex(), TenantContext.getTenantSchema())
        return sql
    }
}
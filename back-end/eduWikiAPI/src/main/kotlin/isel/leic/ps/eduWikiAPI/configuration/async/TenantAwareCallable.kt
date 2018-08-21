package isel.leic.ps.eduWikiAPI.configuration.async

import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import java.util.concurrent.Callable

/**
 * Simple Callable wrapper that sets and clears provided context for the background thread
 */
class TenantAwareCallable<T>(
        private val task: Callable<T>,
        private val context: String?
) : Callable<T> {

    override fun call(): T {
        if(context != null)
            TenantContext.setTenantSchema(context)

        try {
            return task.call()
        } finally {
            TenantContext.resetTenantSchema()
        }
    }
}
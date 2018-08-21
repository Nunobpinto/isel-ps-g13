package isel.leic.ps.eduWikiAPI.configuration.async

import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import java.util.concurrent.Callable
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.util.concurrent.ListenableFuture
import java.util.concurrent.Future

/**
 * Custom TaskPoolExecutor that stores scoped information alongside tasks
 */
class TenantAwarePoolExecutor : ThreadPoolTaskExecutor() {

    override fun <T> submit(task: Callable<T>): Future<T> =
        super.submit(TenantAwareCallable(task, TenantContext.getTenantSchema()))

    override fun <T> submitListenable(task: Callable<T>): ListenableFuture<T> =
        super.submitListenable(TenantAwareCallable(task, TenantContext.getTenantSchema()))
}
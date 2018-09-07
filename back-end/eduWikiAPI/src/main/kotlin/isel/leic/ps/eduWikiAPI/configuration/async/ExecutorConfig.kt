package isel.leic.ps.eduWikiAPI.configuration.async

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
class ExecutorConfig : AsyncConfigurerSupport() {

    override fun getAsyncExecutor(): Executor = tenantAwarePoolExecutor()

    @Bean
    fun tenantAwarePoolExecutor() : ThreadPoolTaskExecutor = TenantAwarePoolExecutor()

}
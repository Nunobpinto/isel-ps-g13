package isel.leic.ps.eduWikiAPI.configuration.async

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import java.util.concurrent.Executor


@Configuration
class ExecutorConfig : AsyncConfigurerSupport() {

    @Bean
    override fun getAsyncExecutor(): Executor = TenantAwarePoolExecutor()
}
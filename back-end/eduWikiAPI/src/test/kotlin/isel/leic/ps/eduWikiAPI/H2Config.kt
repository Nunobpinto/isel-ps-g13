package isel.leic.ps.eduWikiAPI

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.apache.tomcat.jni.SSL.setPassword
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import javax.sql.DataSource


@Configuration
@PropertySource("classpath:test.properties")
class H2Config {
    @Autowired
    lateinit var env: Environment

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName")!!)
        dataSource.url = env.getProperty("jdbc.url")
        dataSource.username = env.getProperty("jdbc.username")
        dataSource.password = env.getProperty("jdbc.password")

        return dataSource
    }
}
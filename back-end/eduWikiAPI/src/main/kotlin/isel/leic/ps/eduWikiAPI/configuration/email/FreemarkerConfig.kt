package isel.leic.ps.eduWikiAPI.configuration.email

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean


/**
 * Sets up FreeMarker Configuration with classpath:/templates
 */
@Configuration
class FreemarkerConfig {

    @Bean
    @Primary
    fun getFreeMarkerConfiguration(): FreeMarkerConfigurationFactoryBean {
        val freeMarkerConfig = FreeMarkerConfigurationFactoryBean()
        freeMarkerConfig.setTemplateLoaderPath("classpath:templates")
        return freeMarkerConfig
    }

}
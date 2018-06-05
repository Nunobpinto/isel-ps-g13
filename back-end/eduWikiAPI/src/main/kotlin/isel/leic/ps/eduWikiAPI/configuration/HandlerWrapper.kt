package isel.leic.ps.eduWikiAPI.configuration

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import javax.annotation.PreDestroy

@Component
@RequestScope
class HandlerWrapper(jdbi: Jdbi) {
    val handle: Handle = jdbi.open()

    @PreDestroy
    fun preDestroy() {
        handle.commit()
    }

    fun close() {
        handle.close()
    }
}
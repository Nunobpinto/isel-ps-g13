package isel.leic.ps.eduWikiAPI

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class EduWikiApiApplication

fun main(args: Array<String>) {
    runApplication<EduWikiApiApplication>(*args)
}

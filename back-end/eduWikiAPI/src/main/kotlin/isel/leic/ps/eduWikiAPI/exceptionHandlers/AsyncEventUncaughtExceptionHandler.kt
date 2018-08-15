package isel.leic.ps.eduWikiAPI.exceptionHandlers

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AsyncEventUncaughtExceptionHandler : AsyncUncaughtExceptionHandler {

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        TODO("Not sure if will simply log the error, or and execute task again")
    }
}
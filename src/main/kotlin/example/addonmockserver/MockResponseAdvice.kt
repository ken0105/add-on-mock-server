package example.addonmockserver

import example.addonmockserver.repository.MockInfo
import example.addonmockserver.repository.MockRepository
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Profile("dev", "local")
@Component
class SampleControllerAdvice(val mockRepository: MockRepository) {

    @Before("execution(public * example.addonmockserver.controller.*.*(..))")
    @Throws(NeedsMockResponseException::class)
    fun checkNecessityForMockResponse() {
        val request = RequestContextHolder
                .currentRequestAttributes()
                .let { it as ServletRequestAttributes }
                .request

        mockRepository.findByPath(request.requestURI)
                ?.let {
                    throw NeedsMockResponseException(it)
                }
    }
}

class NeedsMockResponseException(val mockInfo: MockInfo) : Exception()
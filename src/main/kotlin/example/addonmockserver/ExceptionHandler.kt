package example.addonmockserver

import example.addonmockserver.repository.MockInfo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handle(e: Exception): ResponseEntity<Any> {
        return if (e is NeedsMockResponseException) {
            buildMockResponse(e.mockInfo)
        } else {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun buildMockResponse(mockInfo: MockInfo): ResponseEntity<Any> {
        return ResponseEntity(
                mockInfo.expectedBody,
                HttpHeaders().apply {
                    this.contentType = MediaType(MediaType.APPLICATION_JSON)
                },
                HttpStatus.valueOf(mockInfo.expectedStatusCode)
        )
    }
}
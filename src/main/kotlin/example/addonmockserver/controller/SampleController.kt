package example.addonmockserver.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class SampleController {
    @GetMapping("/real")
    fun real(): ResponseEntity<Message> {
        return fixedResponse
    }

    @GetMapping("/mock")
    fun mock(): ResponseEntity<Message> {
        return fixedResponse
    }
}

val fixedResponse = ResponseEntity(
        Message("This is a real response."),
        HttpStatus.OK)

data class Message(val message: String)
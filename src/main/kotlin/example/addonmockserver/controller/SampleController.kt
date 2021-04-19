package example.addonmockserver.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class SampleController {
    @GetMapping("/users")
    fun getUsers(): ResponseEntity<Users> {
        return ResponseEntity(
            Users(listOf("tanaka", "yamada", "iwata")),
            HttpStatus.OK
        )

    }
}

data class Users(val users: List<String>)
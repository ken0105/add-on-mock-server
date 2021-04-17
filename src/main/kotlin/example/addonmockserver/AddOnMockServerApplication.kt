package example.addonmockserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AddOnMockServerApplication

fun main(args: Array<String>) {
    runApplication<AddOnMockServerApplication>(*args)
}

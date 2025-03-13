package ru.itis.masterbi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MasterBiApplication

fun main(args: Array<String>) {
	runApplication<MasterBiApplication>(*args)
}

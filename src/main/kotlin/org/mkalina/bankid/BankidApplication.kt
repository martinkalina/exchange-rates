package org.mkalina.bankid

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankidApplication

fun main(args: Array<String>) {
	runApplication<BankidApplication>(*args)
}

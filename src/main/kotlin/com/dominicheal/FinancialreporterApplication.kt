package com.dominicheal

import com.dominicheal.configuration.JpaConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@Import(JpaConfiguration::class)
@SpringBootApplication
class FinancialreporterApplication

fun main(args: Array<String>) {
    SpringApplication.run(FinancialreporterApplication::class.java, *args)
}

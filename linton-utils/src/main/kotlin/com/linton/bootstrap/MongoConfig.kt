package com.linton.bootstrap

import io.micronaut.context.annotation.Property
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class MongoConfig(@Property(name = "linton.environment") private val lintonEnvironment:String) {

    val database = "linton-$lintonEnvironment"

    init {
        log.info("Using MongoDB database: $database")
    }

    companion object {
        private val log = LoggerFactory.getLogger(MongoConfig::class.java)
    }
}

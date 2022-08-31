package com.linton.utils

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.reflect.KClass

object Json {
    val jacksonObjectMapper = jacksonObjectMapper()

    init {
        jacksonObjectMapper.registerModule(JavaTimeModule())
        jacksonObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}

fun Any.toJsonString(): String {
    return Json.jacksonObjectMapper.writeValueAsString(this)
}

fun <T : Any> String.toPojo(cls: KClass<T>): T {
    return Json.jacksonObjectMapper.readValue(this, cls.java)
}

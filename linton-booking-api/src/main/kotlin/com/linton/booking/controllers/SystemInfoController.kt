package com.linton.booking.controllers

import io.micronaut.context.annotation.Property
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class SystemInfoController(
    @Property(name = "linton.version")
    private val lintonVersion: String
) {

    @Get("/public/system-info")
    fun systemInfo(): SystemInfo {
        return SystemInfo(
            bookingApiPodId = System.getenv("HOSTNAME"),
            bookingApiPodVersion = lintonVersion
        )
    }
}

@Introspected
data class SystemInfo(
    val bookingApiPodId: String,
    val bookingApiPodVersion: String
)
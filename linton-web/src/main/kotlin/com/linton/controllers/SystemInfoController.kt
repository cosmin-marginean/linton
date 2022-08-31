package com.linton.controllers

import com.linton.apis.BookingApi
import com.linton.apis.BookingApiSystemInfo
import io.micronaut.context.annotation.Property
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import reactor.core.publisher.Mono

@Controller
class SystemInfoController(
    @Property(name = "linton.version")
    private val lintonVersion: String,

    private val bookingApi: BookingApi,
) {

    @Get("/public/system-info")
    fun systemInfo(): Mono<SystemInfo> {
        return bookingApi.systemInfo()
            .map {
                SystemInfo(
                    webPodId = System.getenv("HOSTNAME"),
                    webPodVersion = lintonVersion,
                    bookingApiSystemInfo = it
                )
            }
    }
}

@Introspected
data class SystemInfo(
    val webPodId: String,
    val webPodVersion: String,
    val bookingApiSystemInfo: BookingApiSystemInfo
)
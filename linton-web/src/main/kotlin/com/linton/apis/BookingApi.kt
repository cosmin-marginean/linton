package com.linton.apis

import com.linton.model.Booking
import io.micronaut.http.annotation.Get
import reactor.core.publisher.Mono

interface BookingApi {

    @Get("/bookings/{guestId}")
    fun getGuestBookings(guestId: String): Mono<List<Booking>>

    @Get("/bookings/list")
    fun listBookings(): Mono<List<Booking>>

    @Get("/public/system-info")
    fun systemInfo(): Mono<BookingApiSystemInfo>
}

data class BookingApiSystemInfo(
    val bookingApiPodId: String,
    val bookingApiPodVersion: String
)

package com.linton.controllers

import com.linton.apis.BookingApi
import com.linton.model.Booking
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View
import reactor.core.publisher.Mono

@Controller
class StaffController(private val bookingApi: BookingApi) {

    @Get("/staff/bookings")
    @View("staff/bookings")
    fun listBookings(): Mono<AllBookingsResponse> {
        return bookingApi.listBookings()
            .map { bookings ->
                AllBookingsResponse(bookings)
            }
    }
}

@Introspected
data class AllBookingsResponse(
    val bookings: List<Booking>
)

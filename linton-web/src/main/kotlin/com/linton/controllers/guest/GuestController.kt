package com.linton.controllers

import com.linton.apis.BookingApi
import com.linton.model.Booking
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View
import reactor.core.publisher.Mono
import java.security.Principal

@Controller
class GuestController(private val bookingApi: BookingApi) {

    @Get("/guest/my-bookings")
    @View("guest/my-bookings")
    fun myBookings(principal: Principal): Mono<DashboardSummary> {
        return bookingApi.getGuestBookings(principal.name)
            .map { bookings ->
                DashboardSummary(bookings)
            }
    }
}

@Introspected
data class DashboardSummary(
    val bookings: List<Booking>
)

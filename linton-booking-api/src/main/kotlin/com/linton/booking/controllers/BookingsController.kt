package com.linton.booking.controllers

import com.linton.booking.repository.BookingRepository
import com.linton.model.Booking
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable


@Controller
class BookingsController(private val bookingRepository: BookingRepository) {

    @Get("/bookings/{guestId}")
    fun guestBookings(@PathVariable("guestId") guestId: String): List<Booking> {
        return bookingRepository.getGuestBookings(guestId)
    }

    @Get("/bookings/list")
    fun listBookings(): List<Booking> {
        return bookingRepository.listBookings()
    }
}
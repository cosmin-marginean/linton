package com.linton.booking.repository

import com.linton.model.Booking
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import jakarta.inject.Singleton
import com.linton.bootstrap.MongoConfig
import com.mongodb.client.model.Filters.eq

@Singleton
class BookingRepository(
    private val mongoConfig: MongoConfig,
    private val mongoClient: MongoClient
) {

    private val collection: MongoCollection<Booking>
        get() = mongoClient.getDatabase(mongoConfig.database).getCollection("bookings", Booking::class.java)

    fun getGuestBookings(guestId: String): List<Booking> {
        return collection.find(eq("guestId", guestId)).toList()
    }

    fun listBookings(): List<Booking> {
        return collection.find().toList()
    }
}

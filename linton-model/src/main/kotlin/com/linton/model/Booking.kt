package com.linton.model

import com.linton.utils.uuid
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Introspected
data class Booking(
    val guestId: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val roomType: RoomType,

    @BsonId
    val id: String = uuid()
) {

    val nights: Long = ChronoUnit.DAYS.between(startDate, endDate)
}

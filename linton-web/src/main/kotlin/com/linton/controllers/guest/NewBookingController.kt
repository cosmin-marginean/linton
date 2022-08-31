package com.linton.controllers.guest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View

@Controller
open class NewBookingController {

    @Get("/guest/new-booking")
    @View("guest/new-booking")
    fun viewNewBooking(): HttpResponse<Any> {
        return HttpResponse.ok()
    }
}

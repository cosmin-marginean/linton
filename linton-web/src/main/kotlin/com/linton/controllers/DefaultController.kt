package com.linton.controllers

import com.linton.security.UserRole
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.ServerAuthentication
import java.net.URI
import java.security.Principal

@Controller
class DefaultController {

    @Get("/")
    fun home(principal: Principal): HttpResponse<Any> {
        val auth = principal as ServerAuthentication
        return if (auth.roles.contains(UserRole.STAFF)) {
            HttpResponse.seeOther(URI("/staff/bookings"))
        } else {
            HttpResponse.seeOther(URI("/guest/my-bookings"))
        }
    }
}
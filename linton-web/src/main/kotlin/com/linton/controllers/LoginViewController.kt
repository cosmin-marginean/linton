package com.linton.controllers

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View

@Controller
class LoginViewController() {

    @Get("/login")
    @View("login")
    fun viewLogin(): HttpResponse<Any> {
        return HttpResponse.ok()
    }
}


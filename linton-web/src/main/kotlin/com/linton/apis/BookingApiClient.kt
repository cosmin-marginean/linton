package com.linton.apis

import io.micronaut.context.annotation.Requires
import io.micronaut.http.client.annotation.Client

@Client(id = "linton-booking-api-lb", path = "/")
@Requires(env = ["k8s"])
interface BookingApiClient : BookingApi

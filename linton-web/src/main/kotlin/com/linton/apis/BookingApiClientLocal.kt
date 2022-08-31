package com.linton.apis

import io.micronaut.context.annotation.Requires
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082")
@Requires(env = ["local"])
interface BookingApiClientLocal : BookingApi

package com.linton.security

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink

@Singleton
class LintonAuthProvider(private val userAuthenticationService: UserAuthenticationService) : AuthenticationProvider {

    override fun authenticate(
        request: HttpRequest<*>,
        authRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse?>? {
        return Mono.create { emitter: MonoSink<AuthenticationResponse?> ->
            val loggedInUser =
                userAuthenticationService.login(authRequest.identity.toString(), authRequest.secret.toString())
            if (loggedInUser != null) {
                emitter.success(AuthenticationResponse.success(loggedInUser.id, listOf(loggedInUser.role)))
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }
    }

}
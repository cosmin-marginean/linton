package com.linton.security

import io.micronaut.core.annotation.Introspected

@Introspected
data class UserPassword(
    val userId: String,
    val passwordHash: String
)
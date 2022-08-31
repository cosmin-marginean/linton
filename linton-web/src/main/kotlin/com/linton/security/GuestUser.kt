package com.linton.security

class GuestUser(
    email: String,
    name: String,
    val address: String,
    val title: String,
    enabled: Boolean = true
) : LintonUser(
    email = email,
    name = name,
    role = UserRole.GUEST,
    enabled = enabled
)
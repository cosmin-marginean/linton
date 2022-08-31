package com.linton.security

class StaffUser(
    email: String,
    name: String,
    enabled: Boolean = true
) : LintonUser(
    email = email,
    name = name,
    role = UserRole.STAFF,
    enabled = enabled
)
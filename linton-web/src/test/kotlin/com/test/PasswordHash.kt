package com.test

import org.mindrot.jbcrypt.BCrypt
import org.testng.annotations.Test

class PasswordHash {

    @Test
    fun hashPassword() {
        val password = "Zepass123!"
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        println(passwordHash)
    }
}
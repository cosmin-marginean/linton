package com.linton.security

import jakarta.inject.Singleton
import org.mindrot.jbcrypt.BCrypt

@Singleton
class UserAuthenticationService(private val userRepository: UserRepository) {

    fun login(email: String, password: String): LintonUser? {
        val user = userRepository.findUserByEmail(email) ?: return null
        val storedPassword = userRepository.getPasswordForUser(user.id) ?: return null
        return if (passwordMatches(password, storedPassword.passwordHash)) {
            return user
        } else {
            null
        }
    }

    private fun passwordMatches(password: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }
}
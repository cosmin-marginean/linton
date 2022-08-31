package com.linton.security

import com.linton.bootstrap.MongoConfig
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import jakarta.inject.Singleton
import org.litote.kmongo.findOne

@Singleton
class UserRepository(
    private val mongoConfig: MongoConfig,
    private val mongoClient: MongoClient
) {

    private val users: MongoCollection<LintonUser>
        get() = mongoClient.getDatabase(mongoConfig.database).getCollection("users", LintonUser::class.java)

    private val passwords: MongoCollection<UserPassword>
        get() = mongoClient.getDatabase(mongoConfig.database).getCollection("passwords", UserPassword::class.java)

    fun findUserByEmail(email: String): LintonUser? {
        return users.findOne(eq("email", email))
    }

    fun getPasswordForUser(userId: String): UserPassword? {
        return passwords.findOne(eq("userId", userId))
    }
}
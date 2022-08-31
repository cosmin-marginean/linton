package com.linton.security

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.linton.utils.uuid
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonId

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role")
@JsonSubTypes(
    JsonSubTypes.Type(value = StaffUser::class, name = UserRole.STAFF),
    JsonSubTypes.Type(value = GuestUser::class, name = UserRole.GUEST)
)
@Introspected
abstract class LintonUser(
    val email: String,
    val name: String,
    val role: String,
    val enabled: Boolean = true,

    @BsonId
    @field:JsonProperty("_id")
    val id: String = uuid()
)
package com.linton.bootstrap

import com.mongodb.MongoClientSettings
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import org.bson.codecs.configuration.CodecRegistry
import org.litote.kmongo.KMongo
import org.litote.kmongo.service.ClassMappingType

/**
 * Required to register KMongo codec, which is required if we want to use data classes efficiently with MongoDB
 */
@Factory
@Requires(classes = [KMongo::class])
class KMongoFactory {

    @Singleton
    fun codecRegistry(): CodecRegistry = ClassMappingType.codecRegistry(MongoClientSettings.getDefaultCodecRegistry())
}

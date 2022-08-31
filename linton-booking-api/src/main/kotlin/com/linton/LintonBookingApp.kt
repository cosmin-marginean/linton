package com.linton

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build().args(*args)
            .packages("com.linton")
            .eagerInitSingletons(true)
            .start()
}


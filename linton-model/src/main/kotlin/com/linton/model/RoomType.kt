package com.linton.model

import com.linton.utils.capitalise

enum class RoomType {
    SINGLE,
    DOUBLE,
    SUITE;

    fun displayName() = name.lowercase().capitalise()
}

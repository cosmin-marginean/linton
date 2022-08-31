package com.linton.utils

import java.util.*

fun uuid(): String {
    return UUID.randomUUID()
        .toString()
        .lowercase(Locale.getDefault())
        .replace("-", "")
}

package com.utopia.health


fun String.toFloatOrDefault(defaultVal: Float): Float {
    return toFloatOrNull() ?: defaultVal
}
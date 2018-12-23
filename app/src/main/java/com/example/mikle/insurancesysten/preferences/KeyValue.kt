package com.example.mikle.insurancesysten.preferences

open class KV<T>(val default: T, val key: IAKey)

class StringKV(default: String, key: IAKey) : KV<String>(default, key)

class BoolKV(default: Boolean, key: IAKey) : KV<Boolean>(default, key)

class IntKV(default: Int, key: IAKey) : KV<Int>(default, key)
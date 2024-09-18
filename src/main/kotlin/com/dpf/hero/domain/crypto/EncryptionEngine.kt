package com.dpf.hero.domain.crypto

interface EncryptionEngine {
    fun encrypt(input: String): String
    fun decrypt(encoded: String): String
}
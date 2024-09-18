package com.dpf.hero.crypto

import com.dpf.hero.domain.crypto.EncryptionEngine
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

@Component
class Base64Encryptor : EncryptionEngine {

    companion object {
        private const val PREFIX = "H4sIAAAAAAAA_"
    }

    override fun encrypt(input: String): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        GZIPOutputStream(byteArrayOutputStream).buffered().use { it.write(input.toByteArray()) }
        val compressedBytes = byteArrayOutputStream.toByteArray()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(compressedBytes).removePrefix(PREFIX)
    }

    override fun decrypt(encoded: String): String {
        val curatedEncode = PREFIX + encoded
        val compressedBytes = Base64.getUrlDecoder().decode(curatedEncode)
        val byteArrayInputStream = ByteArrayInputStream(compressedBytes)
        return GZIPInputStream(byteArrayInputStream).bufferedReader().use { it.readText() }
    }
}
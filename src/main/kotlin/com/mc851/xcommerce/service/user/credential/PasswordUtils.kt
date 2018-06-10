package com.mc851.xcommerce.service.user.credential

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import java.util.UUID
import kotlin.experimental.or
import kotlin.experimental.xor

class PasswordUtils {
    companion object {
        private const val ALGORITHM = "SHA-512"

        fun hashpw(pw: String): String {
            return concatenateAndGenerateHash(pw, generateSaltBytes())
        }

        fun checkpw(target: String, check: String): Boolean {
            val saltBytes = Base64.getDecoder().decode(check.substring(0, 24))
            val hash = concatenateAndGenerateHash(target, saltBytes)
            return equalsNoShortCircuit(hash, check)
        }

        private fun concatenateAndGenerateHash(pw: String, saltBytes: ByteArray): String {
            val inBytes = pw.toByteArray(StandardCharsets.UTF_8)
            val digest: MessageDigest

            try {
                digest = MessageDigest.getInstance(ALGORITHM)
            } catch (e: NoSuchAlgorithmException) {
                throw IllegalStateException(String.format("Could not get instance of %s algorithm", ALGORITHM))
            }

            val bufferToDigest = ByteBuffer.wrap(ByteArray(saltBytes.size + inBytes.size)) // do bounds check?
            val finalInput = bufferToDigest.put(inBytes).put(saltBytes).array()
            val out = digest.digest(finalInput)
            val encoder = Base64.getEncoder()
            return encoder.encodeToString(saltBytes) + encoder.encodeToString(out)
        }

        private fun generateSaltBytes(): ByteArray {
            val uuid = UUID.randomUUID()
            return ByteBuffer.wrap(ByteArray(16)).putLong(uuid.leastSignificantBits).putLong(uuid.mostSignificantBits)
                .array()
        }

        private fun equalsNoShortCircuit(first: String, second: String): Boolean {
            val firstBytes = first.toByteArray(StandardCharsets.UTF_8)
            val secondBytes = second.toByteArray(StandardCharsets.UTF_8)

            if (firstBytes.size != secondBytes.size) {
                return false
            }

            var ret: Byte = 0
            for (i in firstBytes.indices) {
                ret = ret or (firstBytes[i] xor secondBytes[i])
            }
            return ret.toInt() == 0
        }
    }
}
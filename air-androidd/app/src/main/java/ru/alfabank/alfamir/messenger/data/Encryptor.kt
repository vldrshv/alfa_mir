package ru.alfabank.alfamir.messenger.data

import android.util.Base64
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import ru.alfabank.alfamir.messenger.data.source.remote.Request
import java.io.StringReader
import java.math.BigInteger
import java.security.*
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encryptor {

    private const val algorithmRSA = "RSA/ECB/PKCS1Padding"

    private var kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
    private var appPublicRSAKey: PublicKey
    private var appPrivateRSAKey: PrivateKey

    private lateinit var servPublicRSAKey: PublicKey

    init {
        kpg.initialize(2048)
        val appRsaPair = kpg.genKeyPair()
        appPublicRSAKey = appRsaPair.public
        appPrivateRSAKey = appRsaPair.private
    }

    fun getAppRsaPublic(): String {
        return appPublicRSAKey.toXml()
    }

    private fun encryptRSA(plain: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algorithmRSA)
        cipher.init(Cipher.ENCRYPT_MODE, servPublicRSAKey)
        return cipher.doFinal(plain)
    }

    private fun decryptRSA(encryptedBytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algorithmRSA)
        cipher.init(Cipher.DECRYPT_MODE, appPrivateRSAKey)
        return cipher.doFinal(encryptedBytes)
    }

    fun setServPublicRSAKey(key: String) {
        val parsed = parseXml(key)
        val modulus = BigInteger(1, Base64.decode(parsed[0], Base64.DEFAULT))
        val exponent = BigInteger(1, Base64.decode(parsed[1], Base64.DEFAULT))
        val spec = RSAPublicKeySpec(modulus, exponent)
        servPublicRSAKey = KeyFactory.getInstance("RSA").generatePublic(spec)
    }

    private fun parseXml(input: String): List<String> {
        val output = arrayListOf<String>()
        val xmlFactory = XmlPullParserFactory.newInstance()
        xmlFactory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        val parser = xmlFactory.newPullParser()
        parser.setInput(StringReader(input))
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.TEXT) {
                output.add(parser.text)
            }
            eventType = parser.next()
        }
        return output
    }

    fun encrypt(value: String): Request {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val key = ByteArray(16)
        val iv = ByteArray(16)
        SecureRandom().nextBytes(key)
        SecureRandom().nextBytes(iv)
        val secretKey = SecretKeySpec(key, "AES")
        val ivParamSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec)

        val encryptedValue = String(Base64.encode(cipher.doFinal(value.toByteArray()), Base64.NO_WRAP))
        val encryptedKey = String(Base64.encode(encryptRSA(secretKey.encoded), Base64.DEFAULT))
        val encryptedVector = String(Base64.encode(encryptRSA(ivParamSpec.iv), Base64.DEFAULT))

        return Request(encryptedValue, encryptedKey, encryptedVector)
    }

    fun decrypt(codeKey: String, codeVector: String, value: String): String {
        var result = value
        if (codeKey.isNotBlank() && codeVector.isNotBlank()) {
            try {
                val keyByte = decryptRSA(Base64.decode(codeKey, Base64.DEFAULT))
                val vectorByte = decryptRSA(Base64.decode(codeVector, Base64.DEFAULT))

                val cipherAes = Cipher.getInstance("AES/CBC/PKCS7Padding")
                val key = SecretKeySpec(keyByte, "AES")
                val iv = IvParameterSpec(vectorByte)
                cipherAes.init(Cipher.DECRYPT_MODE, key, iv)
                result = String(cipherAes.doFinal(Base64.decode(value, Base64.NO_WRAP)))
            } catch (e: Exception) {
                Log.e("Encryptor", "decrypt error: ${e.message}")
            }
        }
        return result
    }

    private fun PublicKey.toXml(): String {
        val spec = KeyFactory.getInstance("RSA").getKeySpec(appPublicRSAKey, RSAPublicKeySpec::class.java)

        val modulus = spec.modulus.toBase64String()
        val exponent = spec.publicExponent.toBase64String()
        return String.format("<RSAKeyValue><Modulus>%s</Modulus><Exponent>%s</Exponent></RSAKeyValue>", modulus, exponent)
    }

    private fun BigInteger.toBase64String(): String {
        val bytes = this.toByteArray()
        val unsigned = if (bytes.size > 128 && bytes[0] == 0.toByte()) bytes.copyOfRange(1, bytes.size) else bytes
        return String(Base64.encode(unsigned, Base64.DEFAULT))
    }
}
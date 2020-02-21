package ru.alfabank.alfamir.main

import android.os.Bundle
import android.util.Base64
import android.util.Log
import org.json.JSONObject
import ru.alfabank.alfamir.base_elements.BaseActivity
import ru.alfabank.alfamir.initialization.presentation.initialization.contract.InitializationContract
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.PrivateKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.inject.Inject

class ExtendedSplashActivity : BaseActivity() {

    @Inject
    lateinit var mPresenter: InitializationContract.Presenter

    private val RSA_ALGORITHM = "RSA/ECB/PKCS1Padding"
    private val KEYSTORE_PROVIDER_1 = "AndroidKeyStore"
    private val KEYSTORE_PROVIDER_2 = "AndroidKeyStoreBCWorkaround"
    private val KEYSTORE_PROVIDER_3 = "AndroidOpenSSL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val input = intent.getStringExtra("PARAM_EXTRA_MESSAGE")
        val json = JSONObject(input)
        val decrypted = decrypt(json.getString("keyAlias"), json.getString("params"))

        finish()
        mPresenter.onSapResponse(JSONObject(decrypted))
    }

    private fun decrypt(alias: String, input: String): String? {

        try {
            val keyStore = KeyStore.getInstance(getKeyStore())
            keyStore.load(null)
            val privateKey = keyStore.getKey(alias, null) as PrivateKey

            val decoded64 = String(Base64.decode(input, Base64.DEFAULT), StandardCharsets.UTF_8)

            val output = Cipher.getInstance(RSA_ALGORITHM)
            output.init(Cipher.DECRYPT_MODE, privateKey)
            val cipherInputStream = CipherInputStream(ByteArrayInputStream(toByteArray(decoded64)), output)

            val values = ArrayList<Byte>()
            var nextByte = cipherInputStream.read()

            while (nextByte != -1) {
                values.add(nextByte.toByte())
                nextByte = cipherInputStream.read()
            }
            val bytes = ByteArray(values.size)
            for (i in bytes.indices) {
                bytes[i] = values[i]
            }

            keyStore.deleteEntry(alias)

            return String(bytes, 0, bytes.size, StandardCharsets.UTF_8)

        } catch (e: Exception) {
            Log.e("AlfaKeyDecryption", e.localizedMessage)
        }

        return null
    }

    private fun getKeyStore(): String {
        return try {
            KeyStore.getInstance(KEYSTORE_PROVIDER_1)
            KEYSTORE_PROVIDER_1
        } catch (err: Exception) {
            try {
                KeyStore.getInstance(KEYSTORE_PROVIDER_2)
                KEYSTORE_PROVIDER_2
            } catch (e: Exception) {
                KEYSTORE_PROVIDER_3
            }
        }
    }

    private fun toByteArray(response: String): ByteArray {
        val byteValues = response.substring(1, response.length - 1).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val bytes = ByteArray(byteValues.size)

        var i = 0
        val len = bytes.size
        while (i < len) {
            bytes[i] = byteValues[i].trim().toByte()
            i++
        }

        return bytes
    }

}
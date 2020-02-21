package ru.alfabank.alfamir.utility.network

import android.os.Environment
import android.util.Base64
import android.util.Log
import io.reactivex.Observable
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.messenger.data.Encryptor
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class Downloader @Inject constructor(private val service: WebService) {

    private val dirName = "Air"
    private val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dirName)

    fun download(guid: String, name: String): Observable<Boolean> {
        val request = RequestFactory.getFile(guid)
        var result: String

        return service.requestX(request)
                .map { response ->
                    JsonWrapper.getFile(response).apply {
                        result = if (!key.isNullOrBlank() && !vector.isNullOrBlank())
                            Encryptor.decrypt(codeKey = key, codeVector = vector, value = base64)
                        else
                            base64
                    }
                    result
                }
                .map { saveToDisk(it, name) }
    }

    private fun saveToDisk(encoded: String, name: String): Boolean {
        if (isExternalStorageWritable()) {

            if (!dir.exists() && !dir.mkdirs()) {
                onCreateDirectoryError()
                return false
            }
            val file = File(dir, name)
            val decoded = Base64.decode(encoded, Base64.NO_WRAP)
            return try {
                val outputStream = FileOutputStream(file)
                outputStream.write(decoded)
                true
            } catch (e: IOException) {
                Log.e("Save to disc", e.message)
                false
            }
        }
        return false
    }

    private fun onCreateDirectoryError() {
        Log.e("Create downloads dir", "cannot create /Downloads/Air/ dirName")
    }

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    fun isDownloaded(name: String): Boolean {
        return File(dir, name).exists()
    }
}
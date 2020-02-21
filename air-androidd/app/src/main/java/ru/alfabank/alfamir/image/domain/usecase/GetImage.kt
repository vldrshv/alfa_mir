package ru.alfabank.alfamir.image.domain.usecase

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import io.reactivex.Observable
import kotlinx.coroutines.*
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.Constants.Initialization.PHOTOS_DISABLED
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.image.data.source.repository.ImageDataSource
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.mapper.ImageMapper
import ru.alfabank.alfamir.image.domain.utility.LinkParser
import ru.alfabank.alfamir.utility.database.DbProvider
import ru.alfabank.alfamir.utility.database.Image
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetImage @Inject constructor(
        private val mImageMapper: ImageMapper,
        private val mImageRepository: ImageRepository,
        private val mLinkParser: LinkParser,
        private val context: App,
        mDbProvider: DbProvider) {

    private val imageDatabase = mDbProvider.getDb().imageDao()
    private val resolution = "400x400"
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun bitmap(url: String, targetDp: Int): Observable<Bitmap> = withContext(Dispatchers.IO) {

        getPathFromDb(url, true)?.also { path ->
            return@withContext Observable.just(BitmapFactory.decodeFile(path))
        }

        if (PHOTOS_DISABLED) {
            return@withContext Observable.just(BitmapFactory.decodeResource(context.resources, R.drawable.ic_file_v))
        }
        downloadByUrl(url, targetDp)
    }

    suspend fun bitmap(guid: String, isHD: Boolean): Observable<Bitmap> = withContext(Dispatchers.IO) {

        getPathFromDb(guid, isHD)?.also { path ->
            return@withContext Observable.just(BitmapFactory.decodeFile(path))
        }

        if (PHOTOS_DISABLED) {
            return@withContext Observable.just(BitmapFactory.decodeResource(context.resources, R.drawable.ic_file_v))
        }
        downloadByGuid(guid, isHD)
    }

    suspend fun string(url: String, targetDp: Int): Observable<String> = withContext(Dispatchers.IO) {
        getPathFromDb(url, true)?.also { path ->
            val bitmap = BitmapFactory.decodeFile(path)
            return@withContext Observable.just(toBase64(bitmap))
        }

        if (PHOTOS_DISABLED) {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_file_v)
            return@withContext Observable.just(toBase64(bitmap))
        }
        downloadStringByUrl(url, targetDp)
    }

    private suspend fun getPathFromDb(guid: String, isHD: Boolean): String? = withContext(Dispatchers.IO) {
        var path: String? = null

        val query = scope.async {
            when (isHD) {
                true -> imageDatabase.getImage(guid, "")
                false -> imageDatabase.getImage(guid, resolution)
            }
        }
        query.await()?.also {
            if (it.path.isNotBlank())
                path = it.path
        }
        path
    }

    private fun downloadByGuid(guid: String, isHD: Boolean): Observable<Bitmap> {
        val res = if (isHD) "" else resolution

        return mImageRepository.getImage(guid, isHD)
                .map { v -> Base64.decode(v, Base64.DEFAULT) }
                .map { v -> BitmapFactory.decodeByteArray(v, 0, v.size) }
                .map { v -> saveToDisk(guid, v, res) }
    }

    private fun downloadByUrl(url: String, targetDp: Int): Observable<Bitmap> {
        val params = mLinkParser.getImageParameters(url, targetDp)
        val picUrl = params.url
        val picHeight = params.height
        val picWidth = params.width
        return mImageRepository.getImage(ImageDataSource.RequestValues(picUrl, picHeight, picWidth, 0))
                .map { v ->
                    var array = ByteArray(0)
                    v.encodedImage?.also {
                        array = Base64.decode(v.encodedImage, Base64.DEFAULT)
                    }
                    array
                }
                .map { v -> BitmapFactory.decodeByteArray(v, 0, v.size) }
                .map { v -> saveToDisk(url, v, "") }
    }

    private fun downloadStringByUrl(url: String, targetDp: Int): Observable<String> {
        val params = mLinkParser.getImageParameters(url, targetDp)
        val picUrl = params.url
        val picHeight = params.height
        val picWidth = params.width
        return mImageRepository.getImage(ImageDataSource.RequestValues(picUrl, picHeight, picWidth, 0))
                .map { v ->
                    val bytes = Base64.decode(v.encodedImage, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    saveToDisk(url, bitmap, "")
                    v.encodedImage
                }
    }

    private fun saveToDisk(guid: String, bitmap: Bitmap, resolution: String): Bitmap {
        val cw = ContextWrapper(context)
        val dir = cw.getDir("images", Context.MODE_PRIVATE)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val rand = System.currentTimeMillis() / (1..100).random()
        val path = File(dir, "$timeStamp $rand.jpg")
        FileOutputStream(path).also {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        scope.launch {
            withContext(Dispatchers.IO) {
                imageDatabase.insert(Image(guid, path.absolutePath, resolution))
            }
        }
        return bitmap
    }

    private suspend fun toBase64(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
    }
}
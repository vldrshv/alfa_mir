package ru.alfabank.alfamir.image.data.source.remote

import io.reactivex.Observable
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.image.data.dto.ImageRaw
import ru.alfabank.alfamir.image.data.source.repository.ImageDataSource
import ru.alfabank.alfamir.messenger.data.Encryptor
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
import javax.inject.Inject


class ImageRemoteDataSource @Inject
internal constructor(private val service: WebService, private val mLogWrapper: LogWrapper) : ImageDataSource {

    override fun getImage(requestValues: ImageDataSource.RequestValues): Observable<ImageRaw> {
        val picUrl = requestValues.picUrl
        val height = requestValues.height
        val width = requestValues.weight
        val isOriginal = requestValues.isIsOriginal

        val request = RequestFactory.formImageRequest(picUrl, width, height, isOriginal)
        return service.requestX(request).map { response ->
            val encodedImageRaw = JsonWrapper.getEncodedImageRaw(response)
            ImageRaw.Builder()
                    .encodedImage(encodedImageRaw!!.encodedImage)
                    .picUrl(picUrl)
                    .picHeight(height)
                    .picWidth(width)
                    .isOriginal(isOriginal)
                    .build()
        }
    }

    override fun uploadImage(encodedImage: String, callback: ImageDataSource.UploadImageCallback) {
        service.uploadImage(encodedImage, object : ImageDataSource.UploadImageCallback {
            override fun onImageUploaded() {
                callback.onImageUploaded()
            }

            override fun onServerNotAvailable() {
                callback.onServerNotAvailable()
            }
        })
    }

    override fun getImage(guid: String, isHd: Boolean): Observable<String> {
        val request = RequestFactory.getImage(guid, isHd)
        return service.requestX(request).map { response ->
            var result: String
            JsonWrapper.getImageAttachment(response)
                    .apply {
                        result = if (!key.isNullOrBlank() && !vector.isNullOrBlank())
                            Encryptor.decrypt(codeKey = key, codeVector = vector, value = base64)
                        else
                            base64
                    }
            result
        }
    }
}

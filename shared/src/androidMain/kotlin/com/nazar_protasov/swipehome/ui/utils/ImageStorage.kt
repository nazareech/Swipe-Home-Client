package com.nazar_protasov.swipehome.ui.utils

import com.nazar_protasov.swipehome.data.local.appContext
import okio.Path
import java.io.File
import java.util.UUID


actual fun saveImageToCache(byteArray: ByteArray): String {
    val fileName = "listing_photo_${UUID.randomUUID()}.jpg"
    val file = File(appContext.cacheDir, fileName)
    file.writeBytes(byteArray)

    return "file://${file.absolutePath}"
}

actual fun readBytesFromCache(filePath: String): ByteArray{
    val cleanPath = filePath.removePrefix("file://")
    return File(cleanPath).readBytes()
}
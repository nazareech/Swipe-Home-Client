package com.nazar_protasov.swipehome.ui.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.writeToFile
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun saveImageToCache(byteArray: ByteArray): String {
    val directory = NSTemporaryDirectory()
    val fileName = "listing_photo_${NSUUID.UUID().UUIDString}.jpg"
    val filePath = directory + fileName

    val nsData = byteArray.usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = byteArray.size.toULong()
        )
    }
    nsData.writeToFile(filePath, atomically = true)

    return "file://$filePath"
}

@OptIn(ExperimentalForeignApi::class)
actual fun readBytesFromCache(filePath: String): ByteArray {
    val cleanPath = filePath.removePrefix("file://")
    val nsData = NSData.dataWithContentsOfFile(cleanPath) ?: return ByteArray(0)

    val byteArray = ByteArray(nsData.length.toInt())
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
    }
    return byteArray
}
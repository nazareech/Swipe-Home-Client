package com.nazar_protasov.swipehome.ui.utils

expect fun saveImageToCache(byteArray: ByteArray): String

expect fun readBytesFromCache(filePath: String): ByteArray
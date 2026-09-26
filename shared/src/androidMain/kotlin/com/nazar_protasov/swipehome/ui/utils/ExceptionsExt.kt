package com.nazar_protasov.swipehome.ui.utils

actual fun Throwable.getLocalizedMessage(): String {
    return this.localizedMessage ?: this.message ?: "Невідома помилка"
}
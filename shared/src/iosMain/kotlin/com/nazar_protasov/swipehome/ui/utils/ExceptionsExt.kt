package com.nazar_protasov.swipehome.ui.utils

actual fun Throwable.getLocalizedMessage(): String {
    return this.message ?: "Невідома помилка"
}
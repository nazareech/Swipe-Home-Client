package com.nazar_protasov.swipehome.ui.utils

enum class CountryPhoneCode(val code: String, val mask: String) {
    // Словник масок для кожної країни
    UKRAINE("+380", "00 000 0000"),
    POLAND("+48", "000 000 000"),
    USA_CANADA("+1", "000 000 0000"),
    UK("+44", "0000 000000"),
    GERMANY("+49", "000 0000000");

    companion object {
        // Допоміжна функція для пошуку маски за кодом
        fun getMaskByCode(code: String): String {
            return entries.find { it.code == code }?.mask ?: UKRAINE.mask
        }

        // Отримати список всіх кодів
        fun getAllCodes(): List<String> {
            return entries.map { it.code }
        }
    }
}
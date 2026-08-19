package com.nazar_protasov.swipehome.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.min

// Кастомна трансформація для форматування у форматі XX XXX XXXX
class PhoneVisualTransformation(private val mask: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Фільтруємо тільки цифри про всяк випадок
        val digits = text.text.filter {it.isDigit()}
        val maxLength = mask.count { it == '0' }
        val trimmed = digits.take(maxLength)

        var out = ""
        var textIndex = 0

        // Генеруємо рядок згідно з маскою
        for (m in mask) {
          if (m == '0'){
              if (textIndex < trimmed.length) {
                  out += trimmed[textIndex]
                  textIndex++
              } else {
                  break // Закінчилися цифри
              }
          } else {
              if (textIndex < trimmed.length) {
                  out += m // Додаємо пробіл/дефіс з маски
              } else {
                  break // Не додаємо висячі пробіли в кінці
              }
          }
        }

        val OffsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformed = 0
                var originalCount = 0
                for (i in mask.indices) {
                    if (originalCount == offset) break
                    if (mask[i] == '0') originalCount++
                    transformed++
                }
                // Максимальна довжина трансформи
                return transformed.coerceIn(0, out.length  )
            }

            override fun transformedToOriginal(offset: Int): Int {
                var original = 0
                for (i in 0 until min(offset, out.length)) {
                    if (mask[i] == '0') original++
                }
                // Безпечне обмеження, щоб уникнути крашів
                return original.coerceIn(0, trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(out), OffsetMapping)
    }
}
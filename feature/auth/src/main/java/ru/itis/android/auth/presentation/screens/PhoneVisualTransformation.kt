package ru.itis.android.auth.presentation.screens

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(10)
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                when (i) {
                    3 -> append(' ')
                    6, 8 -> append('-')
                }
                append(c)
            }
        }
        return TransformedText(AnnotatedString(formatted), PhoneOffsetMapping)
    }

    private object PhoneOffsetMapping : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int = when {
            offset <= 3 -> offset
            offset <= 6 -> offset + 1
            offset <= 8 -> offset + 2
            else -> offset + 3
        }

        override fun transformedToOriginal(offset: Int): Int = when {
            offset <= 3 -> offset
            offset <= 7 -> offset - 1
            offset <= 10 -> offset - 2
            else -> offset - 3
        }
    }
}

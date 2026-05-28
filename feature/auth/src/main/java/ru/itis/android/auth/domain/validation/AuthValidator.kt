package ru.itis.android.auth.domain.validation

import java.util.regex.Pattern

object AuthValidator {

    private val EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")

    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{6,}$")

    fun validatePhone(digits: String): Boolean =
        digits.length == 10 &&
            digits.all { it.isDigit() } &&
            digits[0] in setOf('4', '8', '9')

    fun validateEmail(email: String): Boolean = EMAIL_PATTERN.matcher(email).matches()
    fun validatePassword(password: String): Boolean = PASSWORD_PATTERN.matcher(password).matches()
}
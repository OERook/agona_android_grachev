package ru.itis.android.auth.domain.validation

import java.util.regex.Pattern

object AuthValidator {
    private val PHONE_PATTERN = Pattern.compile("^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$")

    private val EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")

    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{6,}$")

    fun validatePhone(phone: String): Boolean = PHONE_PATTERN.matcher(phone).matches()
    fun validateEmail(email: String): Boolean = EMAIL_PATTERN.matcher(email).matches()
    fun validatePassword(password: String): Boolean = PASSWORD_PATTERN.matcher(password).matches()
}